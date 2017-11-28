/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common;


import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ClassLoaderUtil {

	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static Class loadClass(String className)
			throws ClassNotFoundException {
		return loadClass(className, getContextClassLoader());
	}

	public static Class loadClass(String className, Class referrer)
			throws ClassNotFoundException {
		ClassLoader classLoader = getReferrerClassLoader(referrer);

		return loadClass(className, classLoader);
	}

	public static Class loadClass(String className, ClassLoader classLoader)
			throws ClassNotFoundException {
		if (className == null) {
			return null;
		}

		if (classLoader == null) {
			return Class.forName(className);
		} else {
			return Class.forName(className, true, classLoader);
		}
	}

	public static Class loadServiceClass(String serviceId)
			throws ClassNotFoundException {
		return loadServiceClass(serviceId, getContextClassLoader());
	}

	public static Class loadServiceClass(String serviceId, Class referrer)
			throws ClassNotFoundException {
		ClassLoader classLoader = getReferrerClassLoader(referrer);

		return loadServiceClass(serviceId, classLoader);
	}

	public static Class loadServiceClass(String serviceId,
			ClassLoader classLoader) throws ClassNotFoundException {
		if (serviceId == null) {
			return null;
		}

		serviceId = "META-INF/services/" + serviceId;

		InputStream istream = getResourceAsStream(serviceId, classLoader);

		if (istream == null) {
			throw new RuntimeException("Could not find " + serviceId);
		}

		String serviceClassName;

		try {
			serviceClassName = StringUtil.trimToEmpty(StreamUtil.readText(
					istream, "UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load " + serviceId, e);
		}

		return ClassLoaderUtil.loadClass(serviceClassName, classLoader);
	}

	public static Class loadServiceClass(String className, String serviceId)
			throws ClassNotFoundException {
		return loadServiceClass(className, serviceId, getContextClassLoader());
	}

	public static Class loadServiceClass(String className, String serviceId,
			Class referrer) throws ClassNotFoundException {
		ClassLoader classLoader = getReferrerClassLoader(referrer);

		return loadServiceClass(className, serviceId, classLoader);
	}

	public static Class loadServiceClass(String className, String serviceId,
			ClassLoader classLoader) throws ClassNotFoundException {
		try {
			if (className != null) {
				return loadClass(className, classLoader);
			}
		} catch (ClassNotFoundException e) {
		}

		return loadServiceClass(serviceId, classLoader);
	}

	private static ClassLoader getReferrerClassLoader(Class referrer) {
		ClassLoader classLoader = null;

		if (referrer != null) {
			classLoader = referrer.getClassLoader();

			if (classLoader == null) {
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}

		return classLoader;
	}

	public static Object newInstance(String className)
			throws ClassNotFoundException {
		return newInstance(loadClass(className));
	}

	public static Object newInstance(String className, Class referrer)
			throws ClassNotFoundException {
		return newInstance(loadClass(className, referrer));
	}

	public static Object newInstance(String className, ClassLoader classLoader)
			throws ClassNotFoundException {
		return newInstance(loadClass(className, classLoader));
	}

	private static Object newInstance(Class clazz) {
		if (clazz == null) {
			return null;
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate class: "
					+ clazz.getName(), e);
		}
	}

	public static Object newServiceInstance(String serviceId)
			throws ClassNotFoundException {
		return newInstance(loadServiceClass(serviceId));
	}

	public static Object newServiceInstance(String serviceId, Class referrer)
			throws ClassNotFoundException {
		return newInstance(loadServiceClass(serviceId, referrer));
	}

	public static Object newServiceInstance(String serviceId,
			ClassLoader classLoader) throws ClassNotFoundException {
		return newInstance(loadServiceClass(serviceId, classLoader));
	}

	public static Object newServiceInstance(String className, String serviceId)
			throws ClassNotFoundException {
		return newInstance(loadServiceClass(className, serviceId));
	}

	public static Object newServiceInstance(String className, String serviceId,
			Class referrer) throws ClassNotFoundException {
		return newInstance(loadServiceClass(className, serviceId, referrer));
	}

	public static Object newServiceInstance(String className, String serviceId,
			ClassLoader classLoader) throws ClassNotFoundException {
		return newInstance(loadServiceClass(className, serviceId, classLoader));
	}

	public static URL[] getResources(String resourceName) {
		LinkedList urls = new LinkedList();
		boolean found = false;

		found = getResources(urls, resourceName, getContextClassLoader(), false);

		if (!found) {
			getResources(urls, resourceName,
					ClassLoaderUtil.class.getClassLoader(), false);
		}

		if (!found) {
			getResources(urls, resourceName, null, true);
		}

		return getDistinctURLs(urls);
	}

	public static URL[] getResources(String resourceName, Class referrer) {
		ClassLoader classLoader = getReferrerClassLoader(referrer);
		LinkedList urls = new LinkedList();

		getResources(urls, resourceName, classLoader, classLoader == null);

		return getDistinctURLs(urls);
	}

	public static URL[] getResources(String resourceName,
			ClassLoader classLoader) {
		LinkedList urls = new LinkedList();

		getResources(urls, resourceName, classLoader, classLoader == null);

		return getDistinctURLs(urls);
	}

	private static boolean getResources(List urlSet, String resourceName,
			ClassLoader classLoader, boolean sysClassLoader) {
		if (resourceName == null) {
			return false;
		}

		Enumeration i = null;

		try {
			if (classLoader != null) {
				i = classLoader.getResources(resourceName);
			} else if (sysClassLoader) {
				i = ClassLoader.getSystemResources(resourceName);
			}
		} catch (IOException e) {
		}

		if ((i != null) && i.hasMoreElements()) {
			while (i.hasMoreElements()) {
				urlSet.add(i.nextElement());
			}

			return true;
		}

		return false;
	}

	private static URL[] getDistinctURLs(LinkedList urls) {
		if ((urls == null) || (urls.size() == 0)) {
			return new URL[0];
		}

		Set urlSet = new HashSet(urls.size());

		for (Iterator i = urls.iterator(); i.hasNext();) {
			URL url = (URL) i.next();

			if (urlSet.contains(url)) {
				i.remove();
			} else {
				urlSet.add(url);
			}
		}

		return (URL[]) urls.toArray(new URL[urls.size()]);
	}

	public static URL getResource(String resourceName) {
		if (resourceName == null) {
			return null;
		}

		ClassLoader classLoader = null;
		URL url = null;

		classLoader = getContextClassLoader();

		if (classLoader != null) {
			url = classLoader.getResource(resourceName);

			if (url != null) {
				return url;
			}
		}

		classLoader = ClassLoaderUtil.class.getClassLoader();

		if (classLoader != null) {
			url = classLoader.getResource(resourceName);

			if (url != null) {
				return url;
			}
		}

		return ClassLoader.getSystemResource(resourceName);
	}

	public static URL getResource(String resourceName, Class referrer) {
		if (resourceName == null) {
			return null;
		}

		ClassLoader classLoader = getReferrerClassLoader(referrer);

		return (classLoader == null) ? ClassLoaderUtil.class.getClassLoader()
				.getResource(resourceName) : classLoader
				.getResource(resourceName);
	}

	public static URL getResource(String resourceName, ClassLoader classLoader) {
		if (resourceName == null) {
			return null;
		}

		return (classLoader == null) ? ClassLoaderUtil.class.getClassLoader()
				.getResource(resourceName) : classLoader
				.getResource(resourceName);
	}

	public static InputStream getResourceAsStream(String resourceName) {
		URL url = getResource(resourceName);

		try {
			if (url != null) {
				return url.openStream();
			}
		} catch (IOException e) {
		}

		return null;
	}

	public static InputStream getResourceAsStream(String resourceName,
			Class referrer) {
		URL url = getResource(resourceName, referrer);

		try {
			if (url != null) {
				return url.openStream();
			}
		} catch (IOException e) {
		}

		return null;
	}

	public static InputStream getResourceAsStream(String resourceName,
			ClassLoader classLoader) {
		URL url = getResource(resourceName, classLoader);

		try {
			if (url != null) {
				return url.openStream();
			}
		} catch (IOException e) {
		}

		return null;
	}

	public static URL[] whichClasses(String className) {
		return getResources(ClassUtil.getClassNameAsResource(className));
	}

	public static URL[] whichClasses(String className, Class referrer) {
		return getResources(ClassUtil.getClassNameAsResource(className),
				referrer);
	}

	public static URL[] whichClasses(String className, ClassLoader classLoader) {
		return getResources(ClassUtil.getClassNameAsResource(className),
				classLoader);
	}

	public static URL whichClass(String className) {
		return getResource(ClassUtil.getClassNameAsResource(className));
	}

	public static URL whichClass(String className, Class referrer) {
		return getResource(ClassUtil.getClassNameAsResource(className),
				referrer);
	}

	public static URL whichClass(String className, ClassLoader classLoader) {
		return getResource(ClassUtil.getClassNameAsResource(className),
				classLoader);
	}
}
