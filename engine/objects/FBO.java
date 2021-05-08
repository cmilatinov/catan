package objects;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL32.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL32.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL32.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL32.glBlitFramebuffer;
import static org.lwjgl.opengl.GL32.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL32.glDrawBuffer;
import static org.lwjgl.opengl.GL32.glReadBuffer;
import static org.lwjgl.opengl.GL32.*;

/**
 * Represents a Frame Buffer Object (FBO).
 */
@SuppressWarnings("unused")
public class FBO implements FreeableObject {

	/**
	 * Represents a single attachment linked to an FBO.
	 */
	private static class Attachment implements FreeableObject {

		/**
		 * The attachment's internal ID assigned by OpenGL.
		 */
		public final int id;

		/**
		 * The OpenGL attachment type.
		 */
		public final int glAttachment;

		/**
		 * Indicates whether this attachment is a texture.
		 */
		public final boolean isTexture;

		/**
		 * Creates an attachment with the specified parameters.
		 */
		public Attachment(int width, int height, int internalFormat, int pixelFormat, int dataType, int glAttachment, boolean isTexture) {
			if (isTexture) {
				this.id = glGenTextures();
				this.glAttachment = glAttachment;
				this.isTexture = true;
				glBindTexture(GL_TEXTURE_2D, id);
				glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, pixelFormat, dataType,
						(ByteBuffer) null);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				glFramebufferTexture2D(GL_FRAMEBUFFER, glAttachment, GL_TEXTURE_2D, id, 0);
				glBindTexture(GL_TEXTURE_2D, 0);
			} else {
				this.id = glGenRenderbuffers();
				this.glAttachment = glAttachment;
				this.isTexture = false;
				glBindRenderbuffer(GL_RENDERBUFFER, id);
				glRenderbufferStorage(GL_RENDERBUFFER, internalFormat, width, height);
				glFramebufferRenderbuffer(GL_FRAMEBUFFER, glAttachment, GL_RENDERBUFFER, id);
			}
		}

		/**
		 * Creates an attachment with the specified parameters.
		 */
		public Attachment(int width, int height, int internalFormat, int pixelFormat, int dataType, int glAttachment, int samples, boolean isTexture) {
			if (isTexture) {
				this.id = glGenTextures();
				this.glAttachment = glAttachment;
				this.isTexture = true;
				glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, id);
				glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, samples, internalFormat, width, height, false);
				glFramebufferTexture2D(GL_FRAMEBUFFER, glAttachment, GL_TEXTURE_2D_MULTISAMPLE, id, 0);
				glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, 0);
			} else {
				this.id = glGenRenderbuffers();
				this.glAttachment = glAttachment;
				this.isTexture = false;
				glBindRenderbuffer(GL_RENDERBUFFER, id);
				glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, internalFormat, width, height);
				glFramebufferRenderbuffer(GL_FRAMEBUFFER, glAttachment, GL_RENDERBUFFER, id);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void destroy() {
			if (isTexture)
				glDeleteTextures(id);
			else
				glDeleteRenderbuffers(id);
		}

	}

	/**
	 * OpenGL's internal ID for the framebuffer object.
	 */
	private final int id;

	/**
	 * The width in pixels of the framebuffer.
	 */
	private final int width;

	/**
	 * The height in pixels of the framebuffer.
	 */
	private final int height;

	/**
	 * The number of samples for each pixel in the framebuffer.
	 */
	private final int samples;

	/**
	 * The list of attachments for this framebuffer.
	 */
	private final List<Attachment> attachments = new ArrayList<>();

	/**
	 * Internal array of draw buffers to use when binding the FBO.
	 */
	private int[] drawBuffers;

	/**
	 * Creates an FBO with the given arguments.
	 * @param id OpenGL's internal ID for the framebuffer object.
	 * @param width The width in pixels of the framebuffer.
	 * @param height The height in pixels of the framebuffer.
	 * @param samples The number of samples for each pixel in the framebuffer.
	 */
	private FBO(int id, int width, int height, int samples) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.samples = samples;
	}

	/**
	 * Adds an attachment to the FBO.
	 * 
	 * @param internalFormat The internal format of the attachment.
	 * @param pixelFormat    The pixel format of the attachment.
	 * @param dataType       The data type of the attachment.
	 * @param glAttachment   The attachment unit number.
	 * @param isTexture      Whether or not this attachment is a texture.
	 * @return {@link FBO} This same instance of this class.
	 */
	public FBO addAttachment(int internalFormat, int pixelFormat, int dataType, int glAttachment, boolean isTexture) {
		attachments.add(samples > 1
				? new Attachment(width, height, internalFormat, pixelFormat, dataType, glAttachment, samples, isTexture)
				: new Attachment(width, height, internalFormat, pixelFormat, dataType, glAttachment, isTexture));
		return this;
	}

	/**
	 * Binds the currently active attachments for this framebuffer. This method
	 * should only be called whenever the attachments for this framebuffer are
	 * changed.
	 * 
	 * @return {@link FBO} This same instance of this class.
	 */
	public FBO bindAttachments() {
		ArrayList<Integer> buffers = new ArrayList<>();
		for (Attachment a : attachments)
			if (a.glAttachment >= GL_COLOR_ATTACHMENT0 && a.glAttachment <= GL_COLOR_ATTACHMENT31)
				buffers.add(a.glAttachment);

		drawBuffers = new int[buffers.size()];
		for (int i = 0; i < buffers.size(); i++)
			drawBuffers[i] = buffers.get(i);

		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glDrawBuffers(drawBuffers);
		return this;
	}

	/**
	 * Binds this framebuffer as the current framebuffer allowing for objects to be
	 * rendered on it.
	 * 
	 * @return {@link FBO} This same instance of this class.
	 */
	public FBO bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glDrawBuffers(drawBuffers);
		return this;
	}

	/**
	 * Unbinds this framebuffer.
	 * 
	 * @return {@link FBO} This same instance of this class.
	 */
	public FBO unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glDrawBuffers(GL_BACK);
		return this;
	}

	/**
	 * Indicates if this framebuffer may be used for rendering.
	 * 
	 * @return <b>boolean</b> True if the framebuffer is complete and functional,
	 *         false otherwise.
	 */
	public boolean isFunctional() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		return glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE;
	}

	/**
	 * Resolves this framebuffer's content onto a destination framebuffer.
	 * 
	 * @param dest          The destination framebuffer.
	 * @param srcAttachment The source attachment from which to blit.
	 * @param dstAttachment The destination attachment to which to blit.
	 * @return {@link FBO} This same instance of this class.
	 */
	public FBO resolve(FBO dest, int srcAttachment, int dstAttachment) {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, id);
		glReadBuffer(srcAttachment);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, dest != null ? dest.id : 0);
		glDrawBuffer(dstAttachment);
		glBlitFramebuffer(0, 0, width, height, 0, 0, dest != null ? dest.width : width,
				dest != null ? dest.height : height, GL_COLOR_BUFFER_BIT, GL_NEAREST);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		for (Attachment a : attachments)
			a.destroy();
		glDeleteFramebuffers(id);
	}

	/**
	 * Creates and returns a Framebuffer Object.
	 * 
	 * @param width   The height in pixels of the framebuffer.
	 * @param height  The width in pixels of the framebuffer.
	 * @param samples The number of samples per pixel the framebuffer is to store.
	 * @return {@link FBO} The resulting framebuffer object.
	 */
	public static FBO create(int width, int height, int samples) {
		int id = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		return new FBO(id, width, height, samples);
	}

}
