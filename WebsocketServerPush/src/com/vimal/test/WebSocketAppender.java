package com.vimal.test;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.websocket.Session;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(category = "Core", name = "WebSocketAppender", elementType = "appender", printObject=true)
public final class WebSocketAppender extends AbstractAppender{

	
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    
	public WebSocketAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
		System.out.println("WEb");
	}
    
	public WebSocketAppender(String name, Filter filter, Layout<? extends Serializable> layout,final boolean ignoreExceptions) {
		super(name, filter, layout,ignoreExceptions);
	}

	 @Override
	    public void append(LogEvent event) {
	        readLock.lock();
	        try {
	            final byte[] bytes = getLayout().toByteArray(event);
	            System.out.write(bytes);
	            String log_message = new String(bytes);
	            System.out.println(AppendWrapper.userSessions);
	            for(Session session : AppendWrapper.userSessions) {
	            	session.getAsyncRemote().sendText(log_message);
	            }
	        } catch (Exception ex) {
	            if (!ignoreExceptions()) {
	                throw new AppenderLoggingException(ex);
	            }
	        } finally {
	            readLock.unlock();
	        }
	    }

	 @PluginFactory
    public static WebSocketAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("otherAttribute") String otherAttribute) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new WebSocketAppender(name, filter, layout, true);
    }
}
