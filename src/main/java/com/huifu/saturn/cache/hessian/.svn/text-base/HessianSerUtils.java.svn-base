/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.cache.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

/**
 * 
 * @author dong.yaojd
 * 
 */
public class HessianSerUtils {

	public byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Hessian2Output output = new Hessian2Output(bos);
		output.setSerializerFactory(getSerializerFactory());
		output.writeObject(obj);
		output.close();
		return bos.toByteArray();
	}

	public Object deserialize(byte[] content) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(content);
		Hessian2Input input = new Hessian2Input(bis);
		input.setSerializerFactory(getSerializerFactory());
		Object obj = input.readObject();
		input.close();
		return obj;
	}
	private static SerializerFactory _serializerFactory = null;
	
	synchronized public static final SerializerFactory getSerializerFactory() {
		if (_serializerFactory == null) {
			_serializerFactory = new SerializerFactory();
			
			//_serializerFactory.addFactory(aliEnumFactory);
			//_serializerFactory.addFactory(new OperationRoleSerializerFactory());
		}
		return _serializerFactory;
	}
}

