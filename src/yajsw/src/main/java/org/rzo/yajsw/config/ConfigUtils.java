/*******************************************************************************
 * Copyright  2015 rzorzorzo@users.sf.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.rzo.yajsw.config;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;

public class ConfigUtils
{

	public static Properties asProperties(Configuration config)
	{
		Properties result = new Properties();
		for (Iterator it = config.getKeys(); it.hasNext();)
		{
			String key = (String) it.next();
			result.setProperty(key, config.getProperty(key).toString());
		}
		return result;
	}

}
