package iid.ai.gui;

import iid.ai.classification.NBTClassifier;
import iid.ai.classification.NBTClassifier.Gender;
import iid.ai.util.FileTokenizer;
import iid.ai.util.Tokenizer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;


public class ClassifierGUI extends JFrame {
	private static final long serialVersionUID = 664434790665125608L;

	public static void main(String[] args) throws IOException {
		NBTClassifier classifier = new NBTClassifier();
		if (args.length == 0) {
			File[] maleFiles = new File("blogstrain/M").listFiles();
			for (File file : maleFiles) {
				classifier.putWords(Gender.MALE,
						FileTokenizer.fileTokenize(file));
			}
			File[] femaleFiles = new File("blogstrain/F").listFiles();
			for (File file : femaleFiles) {
				classifier.putWords(Gender.FEMALE,
						FileTokenizer.fileTokenize(file));
			}
		}
		ClassifierGUI gui = new ClassifierGUI(classifier);
		gui.setVisible(true);
	}

	class Controller implements ActionListener, DocumentListener {
		private NBTClassifier classifier;
		private String classifiedString;
		private Gender prediction;

		public Controller(NBTClassifier c) {
			classifier = c;
			classifyButton.addActionListener(this);
			wrongButton.addActionListener(this);
			correctButton.addActionListener(this);
			skipButton.addActionListener(this);
			textArea.getDocument().addDocumentListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			Object s = e.getSource();
			if (s.equals(classifyButton)) {
				classifiedString = textArea.getText().replace("\n", " ")
						.replace("\r", " ");
				prediction = classifier.classify(Tokenizer
						.tokenize(classifiedString));
				resultLabel.setText("The message is classified as: "
						+ prediction
						+ ".\nUse the buttons below to give feedback.");
				correctButton.setEnabled(true);
				wrongButton.setEnabled(true);
				skipButton.setEnabled(true);
				textArea.setEnabled(false);
				classifyButton.setEnabled(false);
			} else if (s.equals(wrongButton)) {
				prediction = (prediction == Gender.MALE) ? Gender.FEMALE
						: Gender.MALE;
				classifier.putWords(prediction,
						Tokenizer.tokenize(classifiedString));
				resultLabel
						.setText("Thanks for the feedback.\nEnter a new message in the area above.");
				correctButton.setEnabled(false);
				wrongButton.setEnabled(false);
				skipButton.setEnabled(false);
				textArea.setEnabled(true);
				textArea.setText("");
			} else if (s.equals(correctButton)) {
				classifier.putWords(prediction,
						Tokenizer.tokenize(classifiedString));
				resultLabel
						.setText("Thanks for the feedback.\nEnter a new message in the area above.");
				correctButton.setEnabled(false);
				wrongButton.setEnabled(false);
				skipButton.setEnabled(false);
				textArea.setEnabled(true);
				textArea.setText("");
			} else if (s.equals(skipButton)) {
				resultLabel.setText("Enter a new message in the area above.");
				correctButton.setEnabled(false);
				wrongButton.setEnabled(false);
				skipButton.setEnabled(false);
				textArea.setEnabled(true);
				textArea.setText("");
			}
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			handleUpdate(e.getDocument());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			handleUpdate(e.getDocument());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			handleUpdate(e.getDocument());
		}

		public void handleUpdate(Document d) {
			boolean enable = textArea.getText().split(" ").length > 5;
			classifyButton.setEnabled(enable);
		}
	}

	private JButton classifyButton;
	private JButton correctButton;
	private JButton wrongButton;
	private JButton skipButton;
	private JLabel resultLabel;
	private JTextArea textArea;

	public ClassifierGUI(NBTClassifier classifier) {
		initializeGUI();
		new Controller(classifier);
		setTitle("Blog gender classifier");
		setResizable(true);
		setSize(new Dimension(500, 250));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent e) {
				System.exit(0);
			}
		});
	}

	private void initializeGUI() {
		classifyButton = new JButton("Guess my gender!");
		classifyButton.setEnabled(false);
		classifyButton.setAlignmentX(CENTER_ALIGNMENT);
		correctButton = new JButton("Correct");
		correctButton.setEnabled(false);
		wrongButton = new JButton("Wrong");
		wrongButton.setEnabled(false);
		skipButton = new JButton("Skip");
		skipButton.setEnabled(false);
		resultLabel = new JLabel("Enter a blog message in the area above");
		resultLabel.setAlignmentX(CENTER_ALIGNMENT);
		textArea = new JTextArea("Enter your blog message here");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		Container buttonPane = new Container();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.add(correctButton);
		buttonPane.add(wrongButton);
		buttonPane.add(skipButton);
		add(textArea);
		add(Box.createRigidArea(new Dimension(0,10)));
		add(classifyButton);
		add(Box.createRigidArea(new Dimension(0,10)));
		add(resultLabel);
		add(buttonPane);
		
	}
}
