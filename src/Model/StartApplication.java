package Model;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class StartApplication {
	
	
	 public static void runWithParameters(int param1, int param2, String param3, String param4, String param5) {
	        SerializerClass serializer = new SerializerClass();
	        MainViewModel mainViewModel = new MainViewModel();
	        Catcher controller = new Catcher();
	        Boolean deserialized = false;

	        controller = serializer.deserializeModules();

	        if (controller == null) {
	            controller = new Catcher();
	        }

	        File file = new File("savedState.ser");
	        if (file.exists() && !file.isDirectory()) {
	            try {
	                serializer.deserializeMainViewModel(mainViewModel);
	                deserialized = true;
	            } catch (Exception e) {
	                System.out.println("Fehler beim Deserialisieren: " + e.getMessage());
	            }
	        }

	        controller.setMainModel(mainViewModel);

	        // Direkte Nutzung der Parameter ohne Benutzereingabe
	        int parameter1 = param1;
	        int parameter2 = param2;
	        String parameter3 = param3;
	        String parameter4 = param4;
	        String parameter5 = param5;

	       // manageMap(controller);
	        new MainClass(parameter1, parameter2, parameter3, parameter4, parameter5, controller, mainViewModel);
	    }

	public static void main(String[] args) {

		SerializerClass serializer = new SerializerClass();
		MainViewModel mainViewModel = new MainViewModel();
		Catcher controller = new Catcher();
		Boolean deserialized = false;

		Scanner scanner = new Scanner(System.in);

		controller = serializer.deserializeModules();

		if (controller == null) {
			controller = new Catcher();
		}

		File file = new File("savedState.ser");
		if (file.exists() && !file.isDirectory()) {
			try {
				serializer.deserializeMainViewModel(mainViewModel);
				deserialized = true;
			} catch (Exception e) {
				System.out.println("Fehler beim Deserialisieren: " + e.getMessage());
			}
		}

		controller.setMainModel(mainViewModel);

		int parameter1;
		try {
		    parameter1 = Integer.parseInt(mainViewModel.getSubBoxPortText().get());
		} catch (NumberFormatException | NullPointerException e) {
		    parameter1 = 0;
		}
		System.out.println(parameter1);
		System.out.print("Möchten Sie den SubBox-Port ändern? (y/n): ");
		if (scanner.nextLine().equalsIgnoreCase("y")) {
		    parameter1 = Integer.parseInt(getInput("set SubBox port: ", scanner, true));
		}

		int parameter2;
		try {
		    parameter2 = Integer.parseInt(mainViewModel.getHostPortText().get());
		} catch (NumberFormatException | NullPointerException e) {
		    parameter2 = 0;
		}
		System.out.println(parameter2);
		System.out.print("Möchten Sie den Host-Port ändern? (y/n): ");
		if (scanner.nextLine().equalsIgnoreCase("y")) {
		    parameter2 = Integer.parseInt(getInput("set Host port: ", scanner, true));
		}

		String parameter3 = (mainViewModel.getHostIPText().get() != null) ? mainViewModel.getHostIPText().get() : "";
		System.out.println(parameter3);
		System.out.print("Möchten Sie die Host-IP ändern? (y/n): ");
		if (scanner.nextLine().equalsIgnoreCase("y")) {
		    parameter3 = getInput("set Host IP: ", scanner, false);
		}

		String parameter4 = (mainViewModel.getControllerIPText().get() != null) ? mainViewModel.getControllerIPText().get() : "";
		System.out.println(parameter4);
		System.out.print("Möchten Sie die Controller-IP ändern? (y/n): ");
		if (scanner.nextLine().equalsIgnoreCase("y")) {
		    parameter4 = getInput("set Controller IP: ", scanner, false);
		}

		String parameter5 = (mainViewModel.getSubBoxText().get() != null) ? mainViewModel.getSubBoxText().get() : "";
		System.out.println(parameter5);
		System.out.print("Möchten Sie die SubBox-Adresse ändern? (y/n): ");
		if (scanner.nextLine().equalsIgnoreCase("y")) {
		    parameter5 = getInput("subBox Address: ", scanner, false);
		}

		manageMap(controller);

		System.out.print("Möchten Sie die Verbindung starten? (y/n): ");
		String startConnection = scanner.nextLine();
		if (startConnection.equalsIgnoreCase("y")) {
			new MainClass(parameter1, parameter2, parameter3, parameter4, parameter5, controller, mainViewModel);
		} else {
			System.out.println("Verbindung nicht gestartet.");
			System.exit(0);
		}

		// Schließen Sie den Scanner
		scanner.close();
	}

	private static String getInput(String prompt, Scanner scanner, boolean isInt) {
	    String input = "";
	    boolean confirm = false;
	    while (!confirm) {
	        System.out.print(prompt);
	        input = scanner.nextLine();

	        if (isInt && !isNumeric(input)) {
	            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Ganzzahl ein.");
	            continue;
	        }

	        System.out.print("Sie haben '" + input + "' eingegeben. Ist das korrekt? (y/n): ");
	        String bestaetigung = scanner.nextLine();
	        if (bestaetigung.equalsIgnoreCase("y")) {
	            confirm = true;
	        }
	    }
	    return input;
	}

	private static boolean isNumeric(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}


	public static void manageMap(Catcher controller) {
		Scanner scanner = new Scanner(System.in);
		HashMap<String, String> moduleMap = controller.getMap();
		boolean continueManaging = true;

		while (continueManaging) {
			System.out.println("\nAktuelle Map:");
			
			for (String key : moduleMap.keySet()) {
				System.out.println(key + " -> " + moduleMap.get(key));
			}

			System.out.println("\nOptionen:");
			System.out.println("1. Hinzufügen");
			System.out.println("2. Löschen");
			System.out.println("3. Beenden");

			System.out.print("Wählen Sie eine Option: ");
			int choice = scanner.nextInt();
			scanner.nextLine();  // Verbraucht das nächste Zeilenende

			switch (choice) {
			case 1:
			    String keyToAdd = "";
			    while (true) {
			        System.out.print("Geben Sie den Schlüssel ein (nur 4 Zeichen erlaubt): ");
			        keyToAdd = scanner.nextLine();

			        if (keyToAdd.length() == 4) {
			            break;  // Verlässt die Schleife, wenn der Schlüssel 4 Zeichen hat.
			        } else {
			            System.out.println("Ungültige Schlüssellänge. Der Schlüssel muss genau 4 Zeichen lang sein.");
			        }
			    }

			    String valueToAdd = "";
			    while (true) {
			        System.out.print("Geben Sie den Wert ein (nur PTF6N1 oder PTF4N4 erlaubt): ");
			        valueToAdd = scanner.nextLine();

			        if (valueToAdd.equals("PTF6N1") || valueToAdd.equals("PTF4N4")) {
			            break;  // Verlässt die Schleife, wenn der Wert korrekt ist.
			        } else {
			            System.out.println("Ungültiger Wert. Bitte geben Sie PTF6N1 oder PTF4N4 ein.");
			        }
			    }

			    moduleMap.put(keyToAdd, valueToAdd);
			    controller.addNewModule(keyToAdd, valueToAdd);
			    System.out.println("Eintrag hinzugefügt.");
			    break;


				case 2:
					System.out.print("Geben Sie den Schlüssel des zu löschenden Eintrags ein: ");
					String keyToDelete = scanner.nextLine();

					if (moduleMap.containsKey(keyToDelete)) {
						moduleMap.remove(keyToDelete);
						controller.removeModule(keyToDelete);
						System.out.println("Eintrag gelöscht.");
					} else {
						System.out.println("Eintrag nicht gefunden.");
					}
					break;

				case 3:
					controller.setTypeAddressMap();
					continueManaging = false;
					break;

				default:
					System.out.println("Ungültige Auswahl. Bitte erneut versuchen.");
			}
		}
	}
}
