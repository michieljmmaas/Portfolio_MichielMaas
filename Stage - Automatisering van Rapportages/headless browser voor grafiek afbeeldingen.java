public boolean getChartImage() {
		Boolean errorless = false;
		try {
			JSONObject profileProperties = AWConverter.CreateJSONFromFile("classes/graphProfile.json");

			// Start de Chrome Driver Headless zodat het de Afbeeldingen kan maken
			String chromeString = "classes/chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", chromeString);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("headless");
			options.addArguments("window-size=1200x600");
			driver = new ChromeDriver(options);

			driver.get("http://localhost:80");
			checkPageIsReady(); // Wachten tot pagina is geladen
			driver.findElement(By.id("NameField")).sendKeys(profileProperties.getString("username"));
			driver.findElement(By.id("PasswordField")).sendKeys(profileProperties.getString("password"));
			driver.findElement(By.id("submit")).click();
			checkPageIsReady(); // Wachten tot pagina is geladen

			List<String> leveranciers = AWECMController.getLeveranicers();
			for (String leverancier : leveranciers) {
				if (!leverancier.contains("Inactief")) {
					String url = "http://localhost:80/Graph/" + leverancier;
					System.out.println("Hij gaat nu naar url : " + url);
					driver.get(url);
					checkPageIsReady();
					Thread.sleep(1000);
					WebElement b = driver.findElement(By.id("mycanvas"));
					File src = b.getScreenshotAs(OutputType.FILE);
					try {
						FileUtils.copyFile(src, new File("classes/Rapportages/ECM/Graph/" + leverancier + ".png"));
					} catch (IOException e) {
						System.out.println("Error bij het opslaan van de grafiek afbeelding van: " + leverancier);
						System.err.println(e.getMessage());
					}
				}
			}
			driver.quit();
			errorless = true;

		} catch (JSONException | IOException e) {
			System.out.println("Error bij het inloggen van de van de Graph Gebruiker. Kan geen afbeelden generen");
			System.err.println("Message :" + e.getMessage());
			driver.quit();
		} catch (InterruptedException e) {
			System.out.println("Error bij wachten op het laden van de grafieken in de headless Chrome Driver");
			System.err.println("Message: " + e.getMessage());
			driver.quit();
		} catch (NoSuchElementException e) {
			System.out.println("Error bij gebruik van de Selenium Chrome Driver.");
			System.out.println(
					"Waarschijnlijk zijn de inlog gegevens verkeerd. Kijk of de gegevens in de classes/graphProfile.json bestand overeen komen met de Admin gegevens");
			driver.quit();
			System.err.println("Message: " + e.getMessage());
		}
		return errorless;

	}