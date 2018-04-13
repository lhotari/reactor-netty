package reactor.ipc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LoggingHandler;

public class ContentSizeLoggingHandler extends LoggingHandler {
	public ContentSizeLoggingHandler(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected String format(ChannelHandlerContext ctx, String eventName, Object arg) {
		if (arg instanceof ByteBuf) {
			return formatByteBuf(ctx, eventName, (ByteBuf) arg);
		}
		else if (arg instanceof ByteBufHolder) {
			return formatByteBufHolder(ctx, eventName, (ByteBufHolder) arg);
		}
		else {
			return formatSimple(ctx, eventName, arg);
		}
	}

	/**
	 * Generates the default log message of the specified event whose argument is a
	 * {@link ByteBuf}.
	 */
	private static String formatByteBuf(ChannelHandlerContext ctx, String eventName,
			ByteBuf msg) {
		String chStr = ctx.channel().toString();
		int length = msg.readableBytes();
		StringBuilder buf = new StringBuilder(
				chStr.length() + 1 + eventName.length() + 10);
		buf.append(chStr).append(' ').append(eventName).append(": ").append(length)
				.append("B");
		return buf.toString();
	}

	/**
	 * Generates the default log message of the specified event whose argument is a
	 * {@link ByteBufHolder}.
	 */
	private static String formatByteBufHolder(ChannelHandlerContext ctx, String eventName,
			ByteBufHolder msg) {
		String chStr = ctx.channel().toString();
		String msgStr = msg.toString();
		ByteBuf content = msg.content();
		int length = content.readableBytes();
		StringBuilder buf = new StringBuilder(
				chStr.length() + 1 + eventName.length() + 2 + msgStr.length() + 10);
		buf.append(chStr).append(' ').append(eventName).append(", ").append(msgStr)
				.append(", ").append(length).append("B");
		return buf.toString();
	}

	/**
	 * Generates the default log message of the specified event whose argument is an
	 * arbitrary object.
	 */
	private static String formatSimple(ChannelHandlerContext ctx, String eventName,
			Object msg) {
		String chStr = ctx.channel().toString();
		String msgStr = String.valueOf(msg);
		StringBuilder buf = new StringBuilder(
				chStr.length() + 1 + eventName.length() + 2 + msgStr.length());
		return buf.append(chStr).append(' ').append(eventName).append(": ").append(msgStr)
				.toString();
	}
}
