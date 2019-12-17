package me.olli.utils.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;

import me.olli.utils.config.exceptions.WrongFileTypeException;
import me.olli.utils.config.settings.ConfigSettings;

public class ConfigHelper implements ConfigSettings {
	
	private final Config c;
	private final File f;
	
	public ConfigHelper(Config config, String path) throws WrongFileTypeException {
		if(!path.endsWith(fileending)) {
			throw new WrongFileTypeException(path + " is not a \"" + fileending + "\" file type");
		}
		this.c = config;
		this.f = new File(path);
	}
	
	public Config getConfig() {
		return c;
	}
	
	
	public String getName() {
		return c.getName();
	}

	public Entry[] getEntrys() {
		return c.getEntrys();
	}
	
	public Entry getEntry(String name) {
		return c.getEntry(name);
	}
	
	public String getValue(String name) {
		return c.getValue(name);
	}
	
	public void setValue(String name, String value) {
		c.setValue(name, value);
	}
	
	public String getDefault(String name) {
		return c.getDefault(name);
	}
	
	public boolean isEntry(String name) {
		return c.isEntry(name);
	}
	
	public void clearValues() {
		c.clearValues();
	}
	
	public void read() {
		if(f.exists() && f.isFile()) {
			this.readconfig();
		} else {
			//konnte keine config finden,
			//erstelle neue.
			this.createNew();
		}
	}
	
	private void readconfig(){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			String l = null;
			clearValues();
			while((l = br.readLine()) != null) {
				String g = l;
				l = l.trim();
				if(l.startsWith(com)) {
					continue;
				} if(l.equals("")) {
					continue;
				}
				
				String[] sp = g.split(":");
				String r = "";
				Entry e = c.getEntry(sp[0]);
				for(int i = 1; i < sp.length; i++) {
					if(i > 1) {
						r += ":";
					}
					r += sp[i];
				}
				r = r.trim();
				if(e.isString()) {
					// wenn string field entferne "" zeichen vorne und hinten und replae \" durch "
					r = r.replace("\\\"", "\"");
					r = r.substring(1, r.length() - 1);
					r = r.trim();
				}
				e.setValue(r);
			}
			Entry[] e = c.getEntrys();
			try {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, true), Charset.forName("UTF-8")));
				boolean b = false;
				for(Entry ee: e) {
					if(ee.getValue() == null) {
						ee.setValue(ee.getDefaultValue());
						
						bw.newLine();
						if(b) {
							bw.newLine();
						}
						if(ee.hasDesc()) {
							if(!b) {
								bw.newLine();
							}
							bw.write(com + ee.getDesc());
							bw.newLine();
						}
						if(!ee.isString()) {
							bw.write(ee.getName() + ": " + ee.getDefaultValue());
						} else {
							bw.write(ee.getName() + ": \"" + ee.getDefaultValue() + "\"");
						}
						b = false;
						if(ee.hasDesc()) {
							b = true;
						}
						
					}
				}
				
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveValues() {
		if(f.exists() && f.isFile()) {
			this.save();
		} else {
			//konnte keine config finden,
			//erstelle neue mit aktuellen values!
			this.createNewWithValues();
		}
	}
	
	private void save() {
		try {
			//config auslesen
			LinkedList<String> lines = new LinkedList<>();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			String line = "";
			
			while((line = br.readLine()) != null) {
				lines.add(line);
			}
			br.close();
			
			//values setten und config saven
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), Charset.forName("UTF-8")));
			for(int i = 0; i < lines.size(); i++) {
				String ll = lines.get(i);
				
				if(ll.equals("") || ll.startsWith(com)) {
					bw.write(ll);
					bw.newLine();
					continue;
				}
				String[] split = ll.split(":");
				String name = split[0].trim();
				Entry ee = c.getEntry(name);
				
				if(!ee.isString()) {
					bw.write(name + ": " + ee.getValue());
				} else {
					bw.write(name + ": \"" + ee.getValue() + "\"");
				}
				if(i != lines.size() - 1) {
					bw.newLine();
				}
			}
			bw.close();
			
		} catch(IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void createNewWithValues() {
		f.delete();
		try {
			f.createNewFile();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), Charset.forName("UTF-8")));
			bw.write(com + "Config File: " + c.getName());
			bw.newLine();
			bw.newLine();
			bw.newLine();
			Entry[] e = c.getEntrys();
			boolean n = true;
			boolean b = false;
			for(Entry ee: e) {
				bw.newLine();
				if(b) {
					bw.newLine();
				}
				if(ee.hasDesc()) {
					if(!n && !b) {
						bw.newLine();
					}
					bw.write(com + ee.getDesc());
					bw.newLine();
				}
				if(!ee.isString()) {
					bw.write(ee.getName() + ": " + ee.getValue());
				} else {
					bw.write(ee.getName() + ": \"" + ee.getValue() + "\"");
				}
				b = false;
				if(ee.hasDesc()) {
					b = true;
				}
				n = false;
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void createNew() {
		f.delete();
		clearValues();
		try {
			f.createNewFile();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), Charset.forName("UTF-8")));
			bw.write(com + "Config File: " + c.getName());
			bw.newLine();
			bw.newLine();
			bw.newLine();
			Entry[] e = c.getEntrys();
			boolean n = true;
			boolean b = false;
			for(Entry ee: e) {
				bw.newLine();
				if(b) {
					bw.newLine();
				}
				if(ee.hasDesc()) {
					if(!n && !b) {
						bw.newLine();
					}
					bw.write(com + ee.getDesc());
					bw.newLine();
				}
				if(!ee.isString()) {
					bw.write(ee.getName() + ": " + ee.getDefaultValue());
				} else {
					bw.write(ee.getName() + ": \"" + ee.getDefaultValue() + "\"");
				}
				b = false;
				if(ee.hasDesc()) {
					b = true;
				}
				n = false;
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.readconfig();
	}
	
	public void refresh() {
		this.read();
	}
	
}
