
config api by OlliPausW

in src/me/olli/Main.java ist ein Beispiel wie man die ConfigAPI anwendet

ebenfalls wurde beispielshaft die "example.conf" automatisch vom der api generiert







wenn man ein seine config neue values hinzufügen möchte dann bitte nur im konstruktor von Config
-> new Config(...
eine weiteres entry hinzufügen und die values in der config durch ein einfaches aufrufen von "public void refresh()" im ConfigHelper generieren lassen