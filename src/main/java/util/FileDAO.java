/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.api.client.util.IOUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Aleksandar
 */
import java.io.IOException;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 *
 * @author Aleksandar
 */
public class FileDAO {

    public void save(InputStream inputStream, File file) throws FileNotFoundException, IOException {
        //we are preparing an output stream so that we can write data to the specified file
        OutputStream output = new FileOutputStream(file);

        
        
    }
}
