package objects;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT31;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;
import static org.lwjgl.opengl.GL30.glRenderbufferStorageMultisample;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class FBO implements FreeableObject {

	private static class Attachment implements FreeableObject {

		public final int id;
		public final int glAttachment;
		public final boolean isTexture;

		/**
		 * Creates an attachment with the specified parameters.
		 */
		public Attachment(int width, int height, int internalFormat, int pixelFormat, int dataType, int glAttachment,
				boolean isTexture) {
			if (isTexture) {
				this.id = glGenTextures();
				this.glAttachment = glAttachment;
				this.isTexture = isTexture;
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
				this.isTexture = isTexture;
				glBindRenderbuffer(GL_RENDERBUFFER, id);
				glRenderbufferStorage(GL_RENDERBUFFER, internalFormat, width, height);
				glFramebufferRenderbuffer(GL_FRAMEBUFFER, glAttachment, GL_RENDERBUFFER, id);
			}
		}

		/**
		 * Creates an attachment with the specified parameters.
		 */
		public Attachment(int width, int height, int internalFormat, int pixelFormat, int dataType, int glAttachment,
				int samples, boolean isTexture) {
			if (isTexture) {
				this.id = glGenTextures();
				this.glAttachment = glAttachment;
				this.isTexture = isTexture;
				glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, id);
				glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, samples, internalFormat, width, height, false);
				glFramebufferTexture2D(GL_FRAMEBUFFER, glAttachment, GL_TEXTURE_2D_MULTISAMPLE, id, 0);
				glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, 0);
			} else {
				this.id = glGenRenderbuffers();
				this.glAttachment = glAttachment;
				this.isTexture = isTexture;
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

	private final int id;
	private final int width;
	private final int height;
	private final int samples;

	private final ArrayList<Attachment> attachments = new ArrayList<Attachment>();

	private int[] drawBuffers;

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
	 * @return [{@link FBO}] This same instance of this class.
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
	 * @return [{@link FBO}] This same instance of this class.
	 */
	public FBO bindAttachments() {
		ArrayList<Integer> buffers = new ArrayList<Integer>();
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
	 * @return [{@link FBO}] This same instance of this class.
	 */
	public FBO bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glDrawBuffers(drawBuffers);
		return this;
	}

	/**
	 * Unbinds this framebuffer.
	 * 
	 * @return [{@link FBO}] This same instance of this class.
	 */
	public FBO unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glDrawBuffers(GL_BACK);
		return this;
	}

	/**
	 * Indicates if this framebuffer may be used for rendering.
	 * 
	 * @return [<b>boolean</b>] True if the framebuffer is complete and functional,
	 *         false otherwise.
	 */
	public boolean isFunctional() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		return glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE;
	}

	/**
	 * Resolves this frambuffer's content onto a destination framebuffer.
	 * 
	 * @param dest          The destination framebuffer.
	 * @param srcAttachment The source attachment from which to blit.
	 * @param dstAttachment The destination attachment to which to blit.
	 * @return [{@link FBO}] This same instance of this class.
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
	 * @return [{@link FBO}] The resulting framebuffer object.
	 */
	public static FBO create(int width, int height, int samples) {
		int id = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		return new FBO(id, width, height, samples);
	}

}
