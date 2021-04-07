package graphics;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageResize.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.BufferUtils.*;


public class Texture {

    private final ByteBuffer image;

    private int w,h,comp;
    public String name;

    private int texID;

    public Texture(String pathToImage) {
        this.name = pathToImage;
        ByteBuffer imageBuffer;
        try {
            imageBuffer = ioResourceToByteBuffer(pathToImage, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            } else {
                //System.out.println("OK with reason: " + stbi_failure_reason());
            }

            //System.out.println("Image width: " + w.get(0));
            //System.out.println("Image height: " + h.get(0));
            //System.out.println("Image components: " + comp.get(0));
            //System.out.println("Image HDR: " + stbi_is_hdr_from_memory(imageBuffer));

            // Decode the image
            image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }

            this.w = w.get(0);
            this.h = h.get(0);
            this.comp = comp.get(0);
        }
        this.texID = createTexture();
    }

    private int createTexture() {
        int texID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        int format;
        if (comp == 3) {
            if ((w & 3) != 0) {
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (w & 1));
            }
            format = GL_RGB;
        } else {
            premultiplyAlpha();

            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

            format = GL_RGBA;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, format, w, h, 0, format, GL_UNSIGNED_BYTE, image);

        ByteBuffer input_pixels = image;
        int        input_w      = w;
        int        input_h      = h;
        int        mipmapLevel  = 0;
        while (1 < input_w || 1 < input_h) {
            int output_w = Math.max(1, input_w >> 1);
            int output_h = Math.max(1, input_h >> 1);

            ByteBuffer output_pixels = memAlloc(output_w * output_h * comp);
            stbir_resize_uint8_generic(
                    input_pixels, input_w, input_h, input_w * comp,
                    output_pixels, output_w, output_h, output_w * comp,
                    comp, comp == 4 ? 3 : STBIR_ALPHA_CHANNEL_NONE, STBIR_FLAG_ALPHA_PREMULTIPLIED,
                    STBIR_EDGE_CLAMP,
                    STBIR_FILTER_MITCHELL,
                    STBIR_COLORSPACE_SRGB
            );

            if (mipmapLevel == 0) {
                stbi_image_free(image);
            } else {
                memFree(input_pixels);
            }

            glTexImage2D(GL_TEXTURE_2D, ++mipmapLevel, format, output_w, output_h, 0, format, GL_UNSIGNED_BYTE, output_pixels);

            input_pixels = output_pixels;
            input_w = output_w;
            input_h = output_h;
        }
        if (mipmapLevel == 0) {
            stbi_image_free(image);
        } else {
            memFree(input_pixels);
        }

        unbind();

        return texID;
    }

    public static void framebufferSizeChanged(long window, int width, int height) {
        glViewport(0, 0, width, height);
    }


    /**
     * Invokes the specified callbacks using the current window and framebuffer sizes of the specified GLFW window.
     *
     * @param window            the GLFW window
     * @param windowSizeCB      the window size callback, may be null
     * @param framebufferSizeCB the framebuffer size callback, may be null
     */
    public static void glfwInvoke(
            long window,
            GLFWWindowSizeCallbackI windowSizeCB,
            GLFWFramebufferSizeCallbackI framebufferSizeCB
    ) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            if (windowSizeCB != null) {
                glfwGetWindowSize(window, w, h);
                windowSizeCB.invoke(window, w.get(0), h.get(0));
            }

            if (framebufferSizeCB != null) {
                glfwGetFramebufferSize(window, w, h);
                framebufferSizeCB.invoke(window, w.get(0), h.get(0));
            }
        }

    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Reads the specified resource and returns the raw data as a ByteBuffer.
     *
     * @param resource   the resource to read
     * @param bufferSize the initial buffer size
     *
     * @return the resource data
     *
     * @throws IOException if an IO error occurs
     */
    public ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);
                while (fc.read(buffer) != -1) {

                }
            }
        } else {
            try (
                    InputStream source = this.getClass().getClassLoader().getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)
            ) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    private void premultiplyAlpha() {
        int stride = w * 4;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y * stride + x * 4;

                float alpha = (image.get(i + 3) & 0xFF) / 255.0f;
                image.put(i + 0, (byte)round(((image.get(i + 0) & 0xFF) * alpha)));
                image.put(i + 1, (byte)round(((image.get(i + 1) & 0xFF) * alpha)));
                image.put(i + 2, (byte)round(((image.get(i + 2) & 0xFF) * alpha)));
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public String getName()
    {
        return this.name;
    }



}
