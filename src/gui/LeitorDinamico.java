package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Toolkit;
import javax.swing.JRadioButton;

public class LeitorDinamico extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4016379082485393058L;
	private JPanel contentPane;
	private JLabel lblTexto;
	private JTextPane txtpnContedo;
	
	private boolean pausado = false;
	private JLabel lblVelocidadeDeLeitura;
	private JSlider slider;
	private JButton btnPausar;
	private JButton btnIniciar;
	protected boolean selecionado = false;
	private JRadioButton rdbtnInfinito;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LeitorDinamico frame = new LeitorDinamico();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public List<String> listePalavrasSeparadas(String texto) {
		return Arrays.asList(texto.split(" "));
	}
	
	private void crieSemiFrases(List<String> palavras) throws InterruptedException {
		int proximoIndice = 0;
		for (int i = proximoIndice; i < palavras.size(); i++) {
			if (Math.abs(palavras.size() - i) >= 2) {// Se tiver pelo menos 2 itens após este item, então execute o código.
				if (pausado == true) {// Se não tiver pausado, então pause.
					break;
				}
				String semiFrase = palavras.get(i) + " " + palavras.get(i + 1) + " " + palavras.get(i + 2);
				proximoIndice = (i + 3);
				Thread.sleep(slider.getValue());
				lblTexto.setText(semiFrase);
			}
			if (Math.abs(palavras.size() - i) == 1) {// Se tiver pelo menos 1 item após este item, então execute o código.
				if (pausado == true) {// Se não tiver pausado, então pause.
					break;
				}
				String semiFrase = palavras.get(i) + " " + palavras.get(i + 1);
				proximoIndice = (i + 2);
				Thread.sleep(slider.getValue());
				lblTexto.setText(semiFrase);
			}
			if (Math.abs(palavras.size() - i) == 0) {// Se não tiver mais itens após este item, então execute o código.
				if (pausado == true) {// Se não tiver pausado, então pause.
					break;
				}
				proximoIndice = (i + 1);
				Thread.sleep(slider.getValue());
				lblTexto.setText(palavras.get(i) + " ");
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public LeitorDinamico() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(LeitorDinamico.class.getResource("/assets/ciclo.png")));
		setTitle("Leitura Dinâmica");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		
		lblTexto = new JLabel("");
		lblTexto.setFont(new Font("DejaVu Sans", Font.PLAIN, 25));
		lblTexto.setForeground(new Color(0, 0, 255));
		lblTexto.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTexto, BorderLayout.NORTH);
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.setFont(new Font("DejaVu Sans", Font.PLAIN, 25));
		btnIniciar.setForeground(new Color(0, 128, 128));
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
					@Override
					protected Void doInBackground() throws Exception {
						LeitorDinamico leitor = new LeitorDinamico();
						List<String> palavras = new ArrayList<>();
						palavras.addAll(leitor.listePalavrasSeparadas(txtpnContedo.getText()));
						if (!selecionado) {
							crieSemiFrases(palavras);
						} else {
							boolean iterando = true;
							while (iterando) {
								for (int i = 0; i < palavras.size(); i++) {
									Thread.sleep(slider.getValue());
									lblTexto.setText(palavras.get(i));
									if (pausado == true) {// Se não tiver pausado, então pause.
										iterando = false;
										break;
									}
								}
							}
						}
						return null;
					}
				};
				worker.execute();
			}
		});
		contentPane.add(btnIniciar, BorderLayout.SOUTH);
		
		btnPausar = new JButton("Pausar/Continuar");
		btnPausar.setForeground(new Color(255, 0, 0));
		btnPausar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pausado == false) {// Se não tiver pausado, então pause.
					pausado = true;
				} else {
					pausado = false;
				}
			}
		});
		contentPane.add(btnPausar, BorderLayout.EAST);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		txtpnContedo = new JTextPane();
		txtpnContedo.setText("Conteúdo");
		scrollPane.setViewportView(txtpnContedo);
		
		slider = new JSlider();
		slider.setInverted(true);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setValue(35);
		slider.setMaximum(175);// Produz 400 palavras por segundo
		slider.setMinimum(35);// Produz 1000 palavras por segundo
		slider.setOrientation(SwingConstants.VERTICAL);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				long quantidadeDePalavrasPorMinuto = (( ((long) 100000 / (long) slider.getValue())) - 171 );
				lblVelocidadeDeLeitura.setText("Quantidade de palavras por minuto: "+String.valueOf(quantidadeDePalavrasPorMinuto)+".");
			}
		});
		scrollPane.setRowHeaderView(slider);
		
		lblVelocidadeDeLeitura = new JLabel("Velocidade de Leitura");
		lblVelocidadeDeLeitura.setForeground(new Color(0, 128, 128));
		lblVelocidadeDeLeitura.setFont(new Font("DejaVu Sans", Font.PLAIN, 18));
		scrollPane.setColumnHeaderView(lblVelocidadeDeLeitura);
		
		rdbtnInfinito = new JRadioButton("Infinito");
		rdbtnInfinito.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnInfinito.isSelected()) {
					selecionado  = true;
				} else {
					selecionado = false;
				}
			}
		});
		rdbtnInfinito.setForeground(new Color(0, 0, 255));
		contentPane.add(rdbtnInfinito, BorderLayout.WEST);
	}

}
