	public String writeTableformat(ArrayList<Document> dList, boolean TablePrefixOption, boolean numberOption) {
		String res = "";

		for (Document tableInput : dList) {
			Set<String> keys = tableInput.keySet();
			ArrayList<String> keyList = new ArrayList<String>(keys);

			String item = keyList.get(0);
			ArrayList<Document> tableItems = (ArrayList<Document>) tableInput.get(item);

			// De hele String wordt in deze StringBuilder gestopt.
			StringBuilder sb = new StringBuilder();

			// Als de Table Prefix Option is geset, dan komt er een tekst boven deze tabel.
			sb.append(divOpen);
			if (TablePrefixOption) {
				sb.append(writeTablePrefix(item, tableItems.size(), numberOption));
			} else {
				sb.append(divOpen + h1Open + " " + h1Close + divClose);
			}

			// Als er items zijn, moet de tabel worden gevuld
			if (tableItems.size() > 0) {

				// De kolommen worden bepaald aan de Keys die de JSON objecten hebben
				Set<String> names = tableItems.get(0).keySet();
				ArrayList<String> namesList = new ArrayList<String>(names);

				// Table Setup
				sb.append(addClass(tableOpen, "table table-striped"));
				sb.append(tableBodyOpen);
				for (String key : namesList) {
					sb.append(tableHeadOpen + key + tableHeadClose);
				}
				sb.append(tableBodyClose);

				// Voeg de items toe
				for (Document d : tableItems) {
					sb.append(tableBodyOpen);
					for (String value : namesList) {
						sb.append(tableItemOpen + String.valueOf(d.get(value)) + tableItemClose);
					}
					sb.append(tableBodyClose);
				}
				sb.append(tableClose);

			}
			// Er zijn geen meldingen, dus komt er een template tekst
			else {
				sb.append(addClass(h4Open, "font-italic") + "Geen " + item + " bekend." + h4Close);
				sb.append("<br>");
			}

			// De String wordt gesloten
			sb.append(divClose);
			res = res + sb.toString();
		}
		return res;
	}