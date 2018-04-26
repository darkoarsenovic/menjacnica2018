package menjacnica.gui.kontroler;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import menjacnica.Menjacnica;
import menjacnica.Valuta;
import menjacnica.gui.DodajKursGUI;
import menjacnica.gui.IzvrsiZamenuGUI;
import menjacnica.gui.MenjacnicaGUI;
import menjacnica.gui.ObrisiKursGUI;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {

	public static Menjacnica sistem = new Menjacnica();
	public static MenjacnicaGUI mg;
	public static JTable table;
	public static Valuta valuta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIKontroler.mg = new MenjacnicaGUI();
					GUIKontroler.mg.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI();
		prozor.setLocationRelativeTo(mg);
		prozor.setVisible(true);
	}

	public static JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.setModel(new MenjacnicaTableModel());
		}
		return table;
	}

	public static void prikaziObrisiKursGUI() {
		if (getTable().getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (getTable().getModel());
			valuta = model.vratiValutu(getTable().getSelectedRow());
			ObrisiKursGUI prozor = new ObrisiKursGUI(valuta);
			prozor.setLocationRelativeTo(mg);
			prozor.setVisible(true);
		}
	}

	public static void prikaziIzvrsiZamenuGUI() {
		if (getTable().getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (getTable().getModel());
			valuta = model.vratiValutu(getTable().getSelectedRow());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(valuta);
			prozor.setLocationRelativeTo(mg);
			prozor.setVisible(true);
		}
	}

	private static void prikaziSveValute() {
		MenjacnicaTableModel model = (MenjacnicaTableModel) (getTable().getModel());
		model.staviSveValuteUModel(sistem.vratiKursnuListu());
	}

	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(mg);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				sistem.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute();
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(mg);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(mg, "Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak",
				JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(mg, "Autori: Bojan Tomic, Darko Arsenovic, Verzija 2.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void unesiKurs(String naziv, String skraceniNaziv, int sifra, double prodajniKurs, double kupovniKurs,
			double srednjiKurs) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceniNaziv);
			valuta.setSifra(sifra);
			valuta.setProdajni(prodajniKurs);
			valuta.setKupovni(kupovniKurs);
			valuta.setSrednji(srednjiKurs);

			// Dodavanje valute u kursnu listu
			sistem.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			prikaziSveValute();

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void obrisiValutu() {
		try {
			sistem.obrisiValutu(valuta);

			prikaziSveValute();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static double izvrsiZamenu(boolean rdbtnProdaja, double iznos) {
		try {
			return sistem.izvrsiTransakciju(valuta, rdbtnProdaja, iznos);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(mg, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
		return 0;
	}
}
