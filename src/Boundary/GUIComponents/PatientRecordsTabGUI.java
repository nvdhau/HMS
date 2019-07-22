package Boundary.GUIComponents;

import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import Boundary.MainForm;
import Boundary.DAO.AppointmentDAOImpl;
import Boundary.DAO.CheckUpRecordDAOImpl;
import Boundary.DAO.PatientDAOImpl;
import Boundary.Helpers.DateTimeHelper;
import Boundary.Helpers.GUIHelper;
import Controller.Authentication;
import Entity.Appointment;
import Entity.CheckUpRecord;
import Entity.Employee;
import Entity.Patient;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PatientRecordsTabGUI extends JPanel {
	
	private JTable tableCheckUpInQueue;
	private JTextField doctorIdTxtBox;	
	private JTextField patientIdTxtBox;
	private JTextField checkUpIdTxtBox;
	private JTextArea medicalProblemsTextArea, checkUpResultTextArea, prescriptionsTextArea;
	
	private DefaultTableModel tm;
	private ListSelectionListener lsl;
	private AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
	private PatientDAOImpl patientDAO = new PatientDAOImpl(); 
	private CheckUpRecordDAOImpl checkUpRecordDAO = new CheckUpRecordDAOImpl(); 
	
	private CheckUpRecord currentCheckUpRecord = null;
	
	private JTextField statusTxtBox;
	private JButton btnSearch;
	private JTextField searchByPatientIdTxtBox;
	
	public void updateTable(ArrayList<CheckUpRecord> checkUpRecordsToDisplay) {
		//remove listener
		tableCheckUpInQueue.getSelectionModel().removeListSelectionListener(lsl);
		
		//array of column names in the table
		String[] columnNames = {"Id", "Patient Id", "Doctor Id", "Date", "Time",
				"Medical Problems", "Diagnostics", "Prescriptions", "Status"};
		
//		//create a DefaultTableModel object
//		Employee loggedInUser = Authentication.getLoggedInEmployee();
//		if(loggedInUser.getRole() == Employee.ADMIN_ROLE)//display all appointments
//			tm = GUIHelper.populateTableModel(columnNames, checkUpRecordDAO.getAllCheckUpRecords());
//		else//receptionist: display in-progress from today appointments only
//			tm = GUIHelper.populateTableModel(columnNames, checkUpRecordDAO.getAllCheckUpRecords());
		
		tm = GUIHelper.populateTableModel(columnNames, checkUpRecordsToDisplay);
		
		tableCheckUpInQueue.setModel(tm);
		
		tableCheckUpInQueue.setRowSorter(new TableRowSorter(tm));
		
		//add listener
		tableCheckUpInQueue.getSelectionModel().addListSelectionListener(lsl);
	}
	
	private void updateCurrentAppointmentInfo(CheckUpRecord checkUpRecord) {
		checkUpIdTxtBox.setText(checkUpRecord.getId() + "");
		doctorIdTxtBox.setText((checkUpRecord.getDoctor() != null) ?
				checkUpRecord.getDoctor().getId() + "" : "");
		patientIdTxtBox.setText(checkUpRecord.getPatient().getId() + "");
		medicalProblemsTextArea.setText(checkUpRecord.getMedicalProblem());
		statusTxtBox.setText(checkUpRecord.getStatus());
		checkUpResultTextArea.setText(checkUpRecord.getCheckupResult() != null ?
				checkUpRecord.getCheckupResult() : "");
		prescriptionsTextArea.setText(checkUpRecord.getPrescriptions() != null ? 
				checkUpRecord.getPrescriptions() : "");
	}
	
	//clear all check up form
	private void clearForm(){
		//reset UIs
		checkUpIdTxtBox.setText("");
		doctorIdTxtBox.setText("");
		patientIdTxtBox.setText("");
		statusTxtBox.setText("");
		medicalProblemsTextArea.setText("");
		checkUpResultTextArea.setText("");
		prescriptionsTextArea.setText("");
		
		currentCheckUpRecord = null;//reset currentCheckUpRecord
	}
	
	public PatientRecordsTabGUI() {
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		scrollPane.setBounds(10, 11, 733, 620);
		add(scrollPane);
		tableCheckUpInQueue = new JTable();

		scrollPane.setViewportView(tableCheckUpInQueue);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Patient Record Detail", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(747, 50, 220, 344);
		add(panel);
		panel.setLayout(null);
			
		JLabel lblPatientID = new JLabel("Doctor ID:");
		lblPatientID.setBounds(6, 54, 78, 16);
		panel.add(lblPatientID);
		lblPatientID.setFont(new Font("Tahoma", Font.PLAIN, 10));
			
		doctorIdTxtBox = new JTextField();
		doctorIdTxtBox.setBounds(98, 48, 116, 22);
		panel.add(doctorIdTxtBox);
		doctorIdTxtBox.setEditable(false);
		doctorIdTxtBox.setColumns(10);			
		
		JLabel patientIdLbl = new JLabel("Patient ID:");
		patientIdLbl.setBounds(6, 81, 78, 16);
		panel.add(patientIdLbl);
		patientIdLbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		patientIdTxtBox = new JTextField();
		patientIdTxtBox.setBounds(98, 75, 116, 22);
		panel.add(patientIdTxtBox);
		patientIdTxtBox.setEditable(false);
		patientIdTxtBox.setColumns(10);
		
		JLabel checkUpIdLbl = new JLabel("Check Up ID:");
		checkUpIdLbl.setBounds(6, 28, 78, 16);
		panel.add(checkUpIdLbl);
		checkUpIdLbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		checkUpIdTxtBox = new JTextField();
		checkUpIdTxtBox.setBounds(98, 22, 116, 22);
		panel.add(checkUpIdTxtBox);
		checkUpIdTxtBox.setEditable(false);
		checkUpIdTxtBox.setColumns(10);
		
		JLabel medicalProblemslbl = new JLabel("Medical Problems:");
		medicalProblemslbl.setBounds(6, 125, 86, 16);
		panel.add(medicalProblemslbl);
		medicalProblemslbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel checkUpResultlbl = new JLabel("Diagnostics:");
		checkUpResultlbl.setBounds(6, 195, 86, 16);
		panel.add(checkUpResultlbl);
		checkUpResultlbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JLabel prescriptionsLbl = new JLabel("Prescriptions:");
		prescriptionsLbl.setBounds(6, 268, 86, 16);
		panel.add(prescriptionsLbl);
		prescriptionsLbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 142, 206, 48);
		panel.add(scrollPane_1);
		
		medicalProblemsTextArea = new JTextArea();
		medicalProblemsTextArea.setEditable(false);
		scrollPane_1.setViewportView(medicalProblemsTextArea);
		medicalProblemsTextArea.setLineWrap(true);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(6, 215, 206, 48);
		panel.add(scrollPane_2);
		
		checkUpResultTextArea = new JTextArea();
		checkUpResultTextArea.setEditable(false);
		scrollPane_2.setViewportView(checkUpResultTextArea);
		checkUpResultTextArea.setLineWrap(true);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(8, 285, 204, 48);
		panel.add(scrollPane_3);
		
		prescriptionsTextArea = new JTextArea();
		prescriptionsTextArea.setEditable(false);
		scrollPane_3.setViewportView(prescriptionsTextArea);
		prescriptionsTextArea.setLineWrap(true);
		
		
		JLabel statusLbl = new JLabel("Status:");
		statusLbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
		statusLbl.setBounds(6, 108, 78, 16);
		panel.add(statusLbl);
		
		statusTxtBox = new JTextField();
		statusTxtBox.setEditable(false);
		statusTxtBox.setColumns(10);
		statusTxtBox.setBounds(98, 102, 116, 22);
		panel.add(statusTxtBox);
		
		//create lsl
		lsl = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				//else try to find the selected checkUpRecord	
				int currId = (int) tableCheckUpInQueue.getValueAt(tableCheckUpInQueue.getSelectedRow(), 0);//1st column
				
				//get the checkUpRecord
				currentCheckUpRecord = checkUpRecordDAO.getCheckUpRecordById(currId);
				
				//check if currenCheckUp is available
				if(currentCheckUpRecord == null) 
					MainForm.showMessage("Cannot get the record.\nPlease try again!");
				else {
					updateCurrentAppointmentInfo(currentCheckUpRecord);
				}
				
			}
		};
		
		searchByPatientIdTxtBox = new JTextField();
		searchByPatientIdTxtBox.setToolTipText("Enter Patient Id");
		searchByPatientIdTxtBox.setBounds(747, 11, 110, 26);
		add(searchByPatientIdTxtBox);
		searchByPatientIdTxtBox.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.setBounds(862, 11, 100, 29);
		add(btnSearch);
		btnSearch.setForeground(Color.BLACK);
		
		JButton btnRefreshCheckUp = new JButton("Refresh Patient Records");
		btnRefreshCheckUp.setBounds(753, 400, 208, 29);
		add(btnRefreshCheckUp);
		btnRefreshCheckUp.setForeground(Color.BLUE);
		
		//Search patient check up records by id
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//get patient id
				int patientId = 0;
				
				try { 
					patientId = Integer.parseInt(searchByPatientIdTxtBox.getText().toString());
					
					updateTable(checkUpRecordDAO.getAllCheckUpRecordsByPatientId(patientId));
				}catch(Exception ex) {
					MainForm.showMessage("Patient Id must be a positive number and cannot be empty.\nPlease try again!");
				}
				
			}
		});
		
		
		btnRefreshCheckUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//reset UIs
				clearForm();
				updateTable(checkUpRecordDAO.getAllHistoryCheckUpRecords());
			}
		});

		clearForm();
		updateTable(checkUpRecordDAO.getAllHistoryCheckUpRecords());
	}
}
