	@Bean
	public RetryTemplate retryTemplate() {
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3); // Aantal pogingen

		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(7200000); // 2 uur

		RetryTemplate template = new RetryTemplate();
		template.setRetryPolicy(retryPolicy);
		template.setBackOffPolicy(backOffPolicy);

		return template;
	}


	public void ECMWorkflow() throws MessagingException, ECManageRapportageException {
		if (AWEmailReceiver.readEmails()) {

			// Als het correcte mailtje binnen is start deze fucntie
			// Update de database met de nieuwe Data
			AWECMDataLoader.update();

			// Maak nieuwe grafieken voor de rapportages
			if (AWChartGenerator.getChartImage()) {

				// Maak alle grafieken aan
				AWECMRapportageProcessor.generatePDF();
				EndingMessage();
			} else {
				System.err.println("Er is een error opgekomen bij het generen van de grafieken.");
				System.err.println("Het ECM Rapportage process is gestopt.");
			}
		} else {
			System.out.println("Systeem probeert het opnieuw.");
			throw new ECManageRapportageException("Mail niet gevonden");
		}

	};