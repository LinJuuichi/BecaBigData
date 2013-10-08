package com.everis.bbd.flume;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Factory for RpcClientFacades.
 */
public class RpcClientFacadeFactory 
{
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(RpcClientFacadeFactory.class.getName());

	/**
	 * Identifier for RpcClientFacade.
	 */
	public static final int RPC_CLIENT_FACADE = 1;

	/**
	 * Identifier for RpcClientFacadeWrapper.
	 */
	public static final int RPC_CLIENT_FACADE_WRAPPER = 2;

	/**
	 * RpcClientFacadeFactory can't be instantiated.
	 */
	private RpcClientFacadeFactory() { }

	/**
	 * Given a string with the name of a RpcClientFacade it returns the id.
	 * 
	 * @param name of the client.
	 * @return the identifier.
	 */
	public static int getClientId(String name)
	{
		if (name.equals("rpcclientfacade") || name.equals("RpcClientFacade"))
		{
			return RpcClientFacadeFactory.RPC_CLIENT_FACADE;
		}
		else if (name.equals("rpcclientfacadewrapper") || name.equals("RpcClientFacadeWrapper") )
		{
			return RpcClientFacadeFactory.RPC_CLIENT_FACADE_WRAPPER;
		}
		return 0;
	}

	/**
	 * Returns the client for the client type.
	 * 
	 * @param type client.
	 * @return client.
	 */
	public static RpcClientFacade getClient(int type)
	{
		RpcClientFacade client = null;
		switch (type) 
		{
		case RPC_CLIENT_FACADE:
			client = new RpcClientFacade();
			break;
		case RPC_CLIENT_FACADE_WRAPPER:
			client = new RpcClientFacadeWrapper();
			break;
		default:
			log.warning("Client type does not exist");
			break;
		}
		return client;
	}

	/**
	 * Returns the client for the client type name.
	 * 
	 * @param name client type.
	 * @return client.
	 */
	public static RpcClientFacade getClient(String name)
	{
		RpcClientFacade client = null;
		int type = RpcClientFacadeFactory.getClientId(name);
		switch (type) 
		{
		case RPC_CLIENT_FACADE:
			client = new RpcClientFacade();
			break;
		case RPC_CLIENT_FACADE_WRAPPER:
			client = new RpcClientFacadeWrapper();
			break;
		default:
			log.warning("Client type does not exist");
			break;
		}
		return client;
	}
	
	/**
	 * Returns the client configured with a failover.
	 * 
	 * @param type client type.
	 * @param props properties configured for failover.
	 * @return client.
	 */
	public static RpcClientFacade getClientWithProperties(int type, Properties props)
	{
		RpcClientFacade client = null;
		switch (type) 
		{
		case RPC_CLIENT_FACADE:
			client = new RpcClientFacade(props);
			break;
		case RPC_CLIENT_FACADE_WRAPPER:
			client = new RpcClientFacadeWrapper();
			break;
		default:
			log.warning("Client type does not exist");
			break;
		}
		return client;
	}
}

