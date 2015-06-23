package org.rzo.netty.ahessian.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * Server side authentication handler.
 * <br>
 * This must be the first handler in the pipeline.
 * 
 *  <br>
 * A typical setup for ServerAuthFilter for TCP/IP socket would be:
 * 
 * <pre>
 * {@link ChannelPipeline} pipeline = ...;
 * 
 *   EncryptedAuthToken token = new EncryptedAuthToken();
 *   token.setAlgorithm("SHA-1");
 *   token.setPassword("test");
 *   ServerAuthFilter auth = new ServerAuthFilter(token);
 *   pipeline.addLast("auth", auth);
 * </pre>
 * 
 */
public class ServerAuthFilter extends ChannelInboundHandlerAdapter
{
	private AuthToken	_token			= null;
	private boolean		_authenticated	= false;
	private static final InternalLogger logger =
        InternalLoggerFactory.getInstance(ServerAuthFilter.class);

	/**
	 * Instantiates a new server auth filter.
	 * 
	 * @param token the token
	 */
	public ServerAuthFilter(AuthToken token)
	{
		setToken(token);
	}

	/**
	 * Sets the token.
	 * 
	 * @param token the new token
	 */
	public void setToken(AuthToken token)
	{
		_token = token;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object e) throws Exception
	{
		if (!_authenticated)
		{
			int result = _token.authenticate(ctx, (ByteBuf) e);
			if ( result == AuthToken.FAILED)
			{
				logger.warn("authentication failed -> close connection");
				ctx.channel().close();
			}
			else if (result == AuthToken.PASSED)
			{
				_authenticated = true;
			}
		}
		else
			ctx.fireChannelRead(e);
	}
	
	@Override
    public void channelInactive(
            ChannelHandlerContext ctx) throws Exception
	{
		_token.disconnected();
		ctx.fireChannelInactive();
	}


}
