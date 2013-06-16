/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dijkstra;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Linh
 */
public class AppFilter extends FileFilter{

    @Override
    public boolean accept(File f) {
	// allow only files with ".gph" extension
	return f.isDirectory() || f.getAbsolutePath().endsWith(".gph");
    }

    @Override
    public String getDescription() {
	// description
	return "Graph file descirption (*.gph)";
    }

}
