/*      AWS Asset Scanner - Scan ALL the Services to see if they are public facing.
        Copyright (C) 2016  Barry McCall AdTheorent LLC

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package com.adtheorent.foss.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.auth.PropertiesCredentials;


/**
 * Utility class for loading and returning properties based on filename. This class assumes properties files are on its
 * classpath, meaning you cannot invoke this across projects.
 */
public final class PropertiesUtils {

    private static final Logger logger = LogManager.getLogger(PropertiesUtils.class);

    /**
     * Gets the Properties by file name.
     *
     * @param propertiesFileName the properties file name
     * @return the Properties
     */
    public static Properties getProperties(final String propertiesFileName) {

        final Properties loadedProperties = new Properties();
        try {
            loadedProperties.load(PropertiesUtils.class.getResourceAsStream("/" + propertiesFileName));
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
        }

        return loadedProperties;
    }

    /**
     * Gets an awscredentials property file for later use
     *
     * @param propertiesFileName the properties file name in resources
     * @return
     * @throws IOException
     */
    public static AWSCredentials getAwsCreds(final String propertiesFileName) throws IOException {
        AWSCredentials creds = null;
        try {
            creds = new PropertiesCredentials(PropertiesUtils.class.getResourceAsStream("/" + propertiesFileName));
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return creds;

    }
}