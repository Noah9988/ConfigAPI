package me.olli;

import me.olli.utils.config.Config;
import me.olli.utils.config.ConfigHelper;
import me.olli.utils.config.Entry;
import me.olli.utils.config.exceptions.WrongFileTypeException;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			
			/** public Entry(String name, String deval, boolean isString, String desc)
			 * @param name ist der name der config zb "einstellungen für den meinkraft server"
			 * @param deval default value
			 * @param isString is diese value ein String
			 * @param desc Beschreibung (wird über dem entry angezeigt)
			 * argumente 3 und 4 sind optional
			 * wenn isString nicht gesetzt wurde ist es automatisch KEIN string
			 */
			Config example_conf = new Config("Example Stuff", new Entry[]{
					new Entry("größe", "165cm", true, "lol bims eine desc.\nlooooool"),
					new Entry("hobbys", "meinkraft spielen", true),
					new Entry("alter", "13", false),
					new Entry("meinkraft namen", "xXSkillerXx, GnomeHD, LemonHaze, oO_MichiPvP_Oo"),
					new Entry("keineahnung", "55", false, "was weiß ich"),
					new Entry("bl", "", true),
					new Entry("", "", false, ""),
					});
			
			ConfigHelper ch = new ConfigHelper(example_conf, "./example.conf");
			ch.read();
			ch.refresh();
			
			
			
			Entry[] e = ch.getEntrys();
			for(Entry ee: e) {
				System.out.println(ee.getName() + " " + ee.getDefaultValue() + " " + ee.getValue());
			}
			
			
		} catch (WrongFileTypeException e) {
			e.printStackTrace();
		}
		
	}
	
}
