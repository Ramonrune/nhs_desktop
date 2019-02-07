/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.physician;

import com.healthsystem.patient.PatientDAO;
import com.healthsystem.patient.PatientModel;
import com.healthsystem.patient.PatientPanel;
import com.healthsystem.util.WindowManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 21/07/2018 12:56:32
 */
public class NFCReader {

    private static java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/physician/Bundle"); // NOI18N

    private static PatientDAO patientDAO = new PatientDAO();
    public NFCReader() {

        //if (setupOk()) {
  
            /*String[] portNames = SerialPortList.getPortNames();

            String port = portNames[0];
            serialPort = new SerialPort(port);
*/
       /*     try {

                if (!serialPort.isOpened()) {
                    serialPort.openPort();
                }
                serialPort.setParams(9600, 8, 1, 0);
                serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);

            } catch (SerialPortException ex) {
                System.out.println(ex);
            }
*/
       // }

    }


    private static SerialPort serialPort;
    
    protected static NFCReaderPanel nfcReaderPanel;
    

    private static String comName;
    public static boolean setupOk() {
       
        String[] portNames = SerialPortList.getPortNames();
        
        if (portNames.length > 0) {
            
            if(serialPort == null){
                serialPort = new SerialPort(portNames[0]);
  
                
                if (!serialPort.isOpened()) {
                    try{
                        serialPort.openPort();
                        serialPort.setParams(9600, 8, 1, 0);
                        serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
                    }catch(Exception e){
                        
                    }
                }
            }
               
            return true;
        }

        
        return false;
    }

    private static String full = "";


    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {

                try {
                    String receivedData = serialPort.readString(event.getEventValue());

                    if (full.length() <= 20) {
                        full += receivedData;

                    }

                    if (full.length() == 21) {

                        
                            String str = full;
                            System.out.println(str);
                            PatientModel patientModel = patientDAO.getPatientByTagCode(str);
                            if(patientModel != null){
                                NFCReaderPanel.imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/loading_patient.gif")));
                                NFCReaderPanel.messageLabel.setText(i18n.getString("NFCReaderPanel.loading"));
                                close();
                                PatientPanel patientPanel = new PatientPanel();
                                patientPanel.setPatientModel(patientModel);
                                WindowManager.changePanel(patientPanel, nfcReaderPanel);
                            }
                            else{
                                
                                PhysicianPanel physicianPanel = new PhysicianPanel();
                                WindowManager.changePanel(physicianPanel, nfcReaderPanel);
                                JOptionPane.showMessageDialog(null, i18n.getString("NFCReaderPanel.notfound"));


                            }
                            
                            nfcReaderPanel.cont = false;
                            full = "";
                        
                    }
                } catch (SerialPortException ex) {
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }

    }
    
     public static void close() {
        System.out.println("testeeee");
         if (serialPort != null) {
            try {
                full= "";
                NFCReaderPanel.cont = true;
                serialPort.removeEventListener();
                serialPort.closePort();
                comName = null;
                serialPort = null;
            } catch (Exception e) {
            }
        }

    }
}
