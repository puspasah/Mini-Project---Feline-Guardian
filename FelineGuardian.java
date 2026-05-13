import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// ============================================================
//  FELINE GUARDIAN — A Digital Health Suite
//  BCA 4th Semester Mini Project
//  Single-file version — compile & run with:
//    javac FelineGuardian.java
//    java FelineGuardian
// ============================================================

public class FelineGuardian {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception e) { e.printStackTrace(); }
            new AppSplashScreen();
        });
    }
}

// ─────────────────────────────────────────────────────────────
//  DATA MODELS
// ─────────────────────────────────────────────────────────────

class Cat implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name, breed, gender, ownerName, ownerPhone;
    private int age;
    private double weight;
    private List<HealthRecord> healthRecords = new ArrayList<>();
    private List<Expense> expenses = new ArrayList<>();

    public Cat(String name, String breed, int age, String gender, double weight,
               String ownerName, String ownerPhone) {
        this.name = name; this.breed = breed; this.age = age;
        this.gender = gender; this.weight = weight;
        this.ownerName = ownerName; this.ownerPhone = ownerPhone;
    }

    public String getName()            { return name; }
    public void   setName(String v)    { name = v; }
    public String getBreed()           { return breed; }
    public void   setBreed(String v)   { breed = v; }
    public int    getAge()             { return age; }
    public void   setAge(int v)        { age = v; }
    public String getGender()          { return gender; }
    public void   setGender(String v)  { gender = v; }
    public double getWeight()          { return weight; }
    public void   setWeight(double v)  { weight = v; }
    public String getOwnerName()       { return ownerName; }
    public void   setOwnerName(String v){ ownerName = v; }
    public String getOwnerPhone()      { return ownerPhone; }
    public void   setOwnerPhone(String v){ ownerPhone = v; }
    public List<HealthRecord> getHealthRecords() { return healthRecords; }
    public List<Expense>      getExpenses()      { return expenses; }
    public void addHealthRecord(HealthRecord r)  { healthRecords.add(r); }
    public void addExpense(Expense e)            { expenses.add(e); }

    public double getTotalExpenses() {
        double t = 0; for (Expense e : expenses) t += e.getAmount(); return t;
    }

    @Override public String toString() { return name + " (" + breed + ")"; }
}

// ─────────────────────────────────────────────────────────────

class HealthRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum Severity { NORMAL, WARNING, EMERGENCY }

    private LocalDate date;
    private double temperature;
    private String hydrationLevel, appetiteLevel, digestionStatus, behaviorChanges, notes, vetName;
    private boolean bloodPresent, extremeWeakness, seizures;
    private Severity severity;
    private String diagnosis, medications;

    public HealthRecord(LocalDate date, double temperature, String hydrationLevel,
                        String appetiteLevel, String digestionStatus, String behaviorChanges,
                        boolean bloodPresent, boolean extremeWeakness, boolean seizures,
                        String notes, String vetName) {
        this.date = date; this.temperature = temperature;
        this.hydrationLevel = hydrationLevel; this.appetiteLevel = appetiteLevel;
        this.digestionStatus = digestionStatus; this.behaviorChanges = behaviorChanges;
        this.bloodPresent = bloodPresent; this.extremeWeakness = extremeWeakness;
        this.seizures = seizures; this.notes = notes; this.vetName = vetName;
        this.severity = classifySeverity();
    }

    private Severity classifySeverity() {
        if (bloodPresent || extremeWeakness || seizures)             return Severity.EMERGENCY;
        if (temperature < 35.5 || temperature > 40.5)               return Severity.EMERGENCY;
        if (hydrationLevel.equals("Severe Dehydration"))             return Severity.EMERGENCY;
        if (appetiteLevel.equals("Not Eating") && behaviorChanges.equals("Lethargic")) return Severity.EMERGENCY;
        if (temperature < 37.5 || temperature > 39.5)               return Severity.WARNING;
        if (hydrationLevel.equals("Mild Dehydration"))               return Severity.WARNING;
        if (appetiteLevel.equals("Reduced"))                         return Severity.WARNING;
        if (!digestionStatus.equals("Normal"))                       return Severity.WARNING;
        if (!behaviorChanges.equals("Normal"))                       return Severity.WARNING;
        return Severity.NORMAL;
    }

    public String getSeverityLabel() {
        switch (severity) {
            case EMERGENCY: return "EMERGENCY";
            case WARNING:   return "WARNING";
            default:        return "NORMAL";
        }
    }

    public String getSeverityAdvice() {
        switch (severity) {
            case EMERGENCY:
                return "CRITICAL ALERT: Your cat requires IMMEDIATE veterinary attention!\n"
                     + "Do not wait — emergency care is needed right now.\n"
                     + (bloodPresent    ? "- Blood detected.\n"            : "")
                     + (extremeWeakness ? "- Extreme weakness detected.\n" : "")
                     + (seizures        ? "- Seizures detected.\n"         : "")
                     + (temperature < 35.5 ? "- Dangerously low temperature.\n"  : "")
                     + (temperature > 40.5 ? "- Dangerously high temperature.\n" : "");
            case WARNING:
                return "WARNING: Some symptoms need monitoring.\n"
                     + "Schedule a vet visit within 24-48 hours.";
            default:
                return "Your cat appears to be in good health.\n"
                     + "Continue regular monitoring and routine vet check-ups.";
        }
    }

    public LocalDate getDate()             { return date; }
    public double    getTemperature()      { return temperature; }
    public String    getHydrationLevel()   { return hydrationLevel; }
    public String    getAppetiteLevel()    { return appetiteLevel; }
    public String    getDigestionStatus()  { return digestionStatus; }
    public String    getBehaviorChanges()  { return behaviorChanges; }
    public boolean   isBloodPresent()      { return bloodPresent; }
    public boolean   isExtremeWeakness()   { return extremeWeakness; }
    public boolean   isSeizures()          { return seizures; }
    public String    getNotes()            { return notes; }
    public Severity  getSeverity()         { return severity; }
    public String    getDiagnosis()        { return diagnosis; }
    public String    getMedications()      { return medications; }
    public String    getVetName()          { return vetName; }
    public void      setDiagnosis(String v)   { diagnosis = v; }
    public void      setMedications(String v) { medications = v; }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    @Override public String toString() { return getFormattedDate() + " | " + getSeverityLabel(); }
}

// ─────────────────────────────────────────────────────────────

class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Category {
        CONSULTATION("Consultation"), MEDICINES("Medicines"),
        LAB_TESTS("Lab Tests"),       INJECTIONS("Injections"),
        DRIP_IV("Drip / IV"),         SURGERY("Surgery"),
        VACCINATION("Vaccination"),   GROOMING("Grooming"),
        FOOD_SUPPLEMENTS("Food/Supplements"), OTHER("Other");

        private final String label;
        Category(String l) { label = l; }
        public String getLabel() { return label; }
        @Override public String toString() { return label; }
    }

    private LocalDate date, dueDate;
    private Category  category;
    private String    description, vetClinic;
    private double    amount;
    private boolean   isPaid;

    public Expense(LocalDate date, Category category, String description,
                   double amount, String vetClinic, boolean isPaid, LocalDate dueDate) {
        this.date = date; this.category = category; this.description = description;
        this.amount = amount; this.vetClinic = vetClinic;
        this.isPaid = isPaid; this.dueDate = dueDate;
    }

    public LocalDate getDate()          { return date; }
    public Category  getCategory()      { return category; }
    public String    getDescription()   { return description; }
    public double    getAmount()        { return amount; }
    public String    getVetClinic()     { return vetClinic; }
    public boolean   isPaid()           { return isPaid; }
    public LocalDate getDueDate()       { return dueDate; }
    public void      setPaid(boolean v) { isPaid = v; }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
    public String getFormattedDueDate() {
        return dueDate == null ? "-" : dueDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
    @Override public String toString() {
        return getFormattedDate() + " | " + category.getLabel() + " | Rs." + String.format("%.2f", amount);
    }
}

// ─────────────────────────────────────────────────────────────
//  DATA MANAGER — File Save / Load
// ─────────────────────────────────────────────────────────────

class DataManager {
    private static final String FILE = "feline_data.dat";

    public static void saveData(List<Cat> cats) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(cats);
        } catch (IOException e) { System.err.println("Save error: " + e.getMessage()); }
    }

    @SuppressWarnings("unchecked")
    public static List<Cat> loadData() {
        if (!new File(FILE).exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            return (List<Cat>) ois.readObject();
        } catch (Exception e) { System.err.println("Load error: " + e.getMessage()); return new ArrayList<>(); }
    }
}

// ─────────────────────────────────────────────────────────────
//  THEME CONSTANTS
// ─────────────────────────────────────────────────────────────

class Theme {
    static final Color BG_DARK  = new Color(15, 25, 45);
    static final Color BG_PANEL = new Color(22, 38, 65);
    static final Color ACCENT   = new Color(255, 200, 80);
    static final Color ACCENT2  = new Color(100, 180, 255);
    static final Color TEXT_MAIN= new Color(220, 235, 255);
    static final Color TEXT_DIM = new Color(130, 160, 200);
    static final Color SUCCESS  = new Color(80, 200, 120);
    static final Color WARNING  = new Color(255, 165, 0);
    static final Color DANGER   = new Color(220, 60, 60);
    static final Color CARD_BG  = new Color(28, 48, 80);

    static JButton smallButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    static JLabel dimLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(TEXT_DIM);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    static void styleField(JTextField f) {
        f.setBackground(new Color(20, 35, 60));
        f.setForeground(TEXT_MAIN);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createLineBorder(new Color(50, 80, 120)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setCaretColor(ACCENT);
    }

    static void styleCombo(JComboBox<?> c) {
        c.setBackground(new Color(20, 35, 60));
        c.setForeground(TEXT_MAIN);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}

// ─────────────────────────────────────────────────────────────
//  SPLASH SCREEN
// ─────────────────────────────────────────────────────────────

class AppSplashScreen extends JWindow {
    public AppSplashScreen() {
        setSize(480, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(15, 30, 55), getWidth(), getHeight(), new Color(30, 60, 90)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("FELINE GUARDIAN", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(new Color(255, 200, 80));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 8, 0));

        JLabel sub = new JLabel("A Digital Health Suite for Your Cat", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        sub.setForeground(new Color(180, 210, 255));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ver = new JLabel("BCA 4th Semester Mini Project  |  v1.0", SwingConstants.CENTER);
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ver.setForeground(new Color(130, 160, 200));
        ver.setAlignmentX(Component.CENTER_ALIGNMENT);
        ver.setBorder(BorderFactory.createEmptyBorder(6, 0, 20, 0));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(false);
        bar.setForeground(new Color(255, 200, 80));
        bar.setBackground(new Color(30, 50, 80));
        bar.setBorderPainted(false);
        bar.setMaximumSize(new Dimension(380, 6));
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loading = new JLabel("Loading...", SwingConstants.CENTER);
        loading.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        loading.setForeground(new Color(150, 180, 220));
        loading.setAlignmentX(Component.CENTER_ALIGNMENT);
        loading.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        panel.add(title); panel.add(sub); panel.add(ver); panel.add(bar); panel.add(loading);
        add(panel);
        setVisible(true);

        final int[] p = {0};
        new Timer(20, null) {{
            addActionListener(e -> {
                p[0] += 2; bar.setValue(p[0]);
                if (p[0] >= 40) loading.setText("Initializing modules...");
                if (p[0] >= 70) loading.setText("Loading health records...");
                if (p[0] >= 90) loading.setText("Almost ready...");
                if (p[0] >= 100) { stop(); dispose(); new Dashboard(DataManager.loadData()); }
            });
            start();
        }};
    }
}

// ─────────────────────────────────────────────────────────────
//  MAIN DASHBOARD
// ─────────────────────────────────────────────────────────────

class Dashboard extends JFrame {
    List<Cat> cats;
    JComboBox<Cat> catSelector;
    JLabel statusLabel;

    Dashboard(List<Cat> cats) {
        this.cats = cats;
        setTitle("Feline Guardian - Digital Health Suite");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(950, 660);
        setMinimumSize(new Dimension(800, 560));
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int r = JOptionPane.showConfirmDialog(Dashboard.this,
                        "Save data and exit?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
                if (r == JOptionPane.YES_OPTION) { DataManager.saveData(cats); System.exit(0); }
                else if (r == JOptionPane.NO_OPTION) System.exit(0);
            }
        });
        setContentPane(buildMain());
        setVisible(true);
    }

    private JPanel buildMain() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_DARK);
        p.add(buildHeader(),   BorderLayout.NORTH);
        p.add(buildSidebar(),  BorderLayout.WEST);
        p.add(buildCenter(),   BorderLayout.CENTER);
        p.add(buildStatusBar(),BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(Theme.BG_PANEL);
        h.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,new Color(50,80,120)),
                BorderFactory.createEmptyBorder(12,18,12,18)));

        JLabel logo = new JLabel("FELINE GUARDIAN");
        logo.setFont(new Font("Georgia", Font.BOLD, 20));
        logo.setForeground(Theme.ACCENT);

        catSelector = new JComboBox<>();
        catSelector.setBackground(Theme.CARD_BG);
        catSelector.setForeground(Theme.TEXT_MAIN);
        catSelector.setFont(new Font("Segoe UI", Font.BOLD, 12));
        catSelector.setPreferredSize(new Dimension(160, 28));
        refreshCats();

        JButton addBtn = Theme.smallButton("+ Add Cat", Theme.ACCENT);
        addBtn.setForeground(Color.BLACK);
        addBtn.addActionListener(e -> addCat());

        JButton remBtn = Theme.smallButton("Remove", new Color(180,60,60));
        remBtn.addActionListener(e -> removeCat());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JLabel cl = new JLabel("Active Cat:");
        cl.setForeground(Theme.TEXT_DIM);
        cl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        right.add(cl); right.add(catSelector); right.add(addBtn); right.add(remBtn);

        h.add(logo, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    private JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setBackground(Theme.BG_PANEL);
        sb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,0,1,new Color(40,65,100)),
                BorderFactory.createEmptyBorder(20,10,20,10)));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setPreferredSize(new Dimension(175, 0));

        JLabel nav = new JLabel("NAVIGATION");
        nav.setFont(new Font("Segoe UI", Font.BOLD, 10));
        nav.setForeground(Theme.TEXT_DIM);
        nav.setAlignmentX(Component.LEFT_ALIGNMENT);
        nav.setBorder(BorderFactory.createEmptyBorder(0,5,10,0));
        sb.add(nav);

        String[][] items = {
            {"Dashboard",       "dashboard"},
            {"Symptom Scanner", "symptom"},
            {"Health History",  "history"},
            {"Expense Tracker", "expense"},
            {"Emergency Info",  "emergency"},
            {"Cat Profile",     "profile"}
        };
        for (String[] item : items) {
            JButton btn = navButton(item[0]);
            final String a = item[1];
            btn.addActionListener(e -> navigate(a));
            sb.add(btn);
            sb.add(Box.createVerticalStrut(6));
        }
        sb.add(Box.createVerticalGlue());
        JLabel ver = new JLabel("v1.0 - BCA Sem 4");
        ver.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        ver.setForeground(new Color(80,110,150));
        ver.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(ver);
        return sb;
    }

    private JPanel buildCenter() {
        JPanel c = new JPanel(new GridBagLayout());
        c.setBackground(Theme.BG_DARK);
        c.setBorder(BorderFactory.createEmptyBorder(22,22,22,22));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(8,8,8,8);

        // Welcome banner
        g.gridx=0; g.gridy=0; g.gridwidth=2; g.weightx=1; g.weighty=0.1;
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(new Color(25,50,85));
        welcome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50,90,140)),
                BorderFactory.createEmptyBorder(14,18,14,18)));
        JLabel wt = new JLabel("Welcome to Feline Guardian");
        wt.setFont(new Font("Georgia", Font.BOLD, 16));
        wt.setForeground(Theme.ACCENT);
        JLabel wi = new JLabel("<html><body style='width:500px'>" +
                (cats.isEmpty() ? "No cats registered yet. Click '+ Add Cat' to get started."
                                : "Managing " + cats.size() + " cat(s). Select a cat to begin monitoring.")
                + "</body></html>");
        wi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        wi.setForeground(Theme.TEXT_DIM);
        welcome.add(wt, BorderLayout.NORTH);
        welcome.add(wi, BorderLayout.CENTER);
        c.add(welcome, g);

        // Feature cards
        g.gridwidth=1; g.weighty=0.45;
        g.gridx=0; g.gridy=1; g.weightx=0.5;
        c.add(featureCard("Symptom Scanner",  "Analyze symptoms. Classifies as Normal, Warning, or Emergency.", "symptom"), g);
        g.gridx=1;
        c.add(featureCard("Expense Tracker",  "Record and manage vet costs. Track spending and payments.", "expense"), g);
        g.gridx=0; g.gridy=2;
        c.add(featureCard("Health History",   "View all medical records — symptoms, treatments, diagnoses.", "history"), g);
        g.gridx=1;
        c.add(featureCard("Emergency Guide",  "Emergency contacts and first-aid for critical situations.", "emergency"), g);
        return c;
    }

    private JPanel featureCard(String title, String desc, String action) {
        JPanel card = new JPanel(new BorderLayout(0,8));
        card.setBackground(Theme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40,70,110)),
                BorderFactory.createEmptyBorder(16,16,16,16)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel tl = new JLabel(title);
        tl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tl.setForeground(Theme.ACCENT2);
        JLabel dl = new JLabel("<html><body style='width:200px'>" + desc + "</body></html>");
        dl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dl.setForeground(Theme.TEXT_DIM);
        JButton ob = Theme.smallButton("Open", new Color(40,80,140));
        ob.addActionListener(e -> navigate(action));
        card.add(tl, BorderLayout.NORTH);
        card.add(dl, BorderLayout.CENTER);
        card.add(ob, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(10,18,35));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1,0,0,0,new Color(40,65,100)),
                BorderFactory.createEmptyBorder(5,14,5,14)));
        statusLabel = new JLabel("Ready. Data file: feline_data.dat");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(Theme.TEXT_DIM);
        JButton sv = Theme.smallButton("Save Now", new Color(60,120,60));
        sv.addActionListener(e -> { DataManager.saveData(cats); setStatus("Data saved."); });
        bar.add(statusLabel, BorderLayout.WEST);
        bar.add(sv, BorderLayout.EAST);
        return bar;
    }

    void navigate(String action) {
        Cat cat = (Cat) catSelector.getSelectedItem();
        if (cat == null && !action.equals("emergency")) {
            JOptionPane.showMessageDialog(this, "Please add and select a cat first.", "No Cat", JOptionPane.WARNING_MESSAGE);
            return;
        }
        switch (action) {
            case "symptom":   new SymptomScanner(this, cat); break;
            case "history":   new HealthHistory(this, cat); break;
            case "expense":   new ExpenseTracker(this, cat); break;
            case "emergency": new EmergencyGuide(this); break;
            case "profile":   new CatProfile(this, cat); break;
        }
    }

    void addCat() {
        JTextField nf=new JTextField(14), bf=new JTextField(14), af=new JTextField(5),
                   wf=new JTextField(5), of=new JTextField(14), pf=new JTextField(14);
        JComboBox<String> gb = new JComboBox<>(new String[]{"Male","Female"});
        JPanel form = new JPanel(new GridLayout(7,2,6,6));
        Object[][] rows = {{"Cat Name:",nf},{"Breed:",bf},{"Age (months):",af},
                           {"Gender:",gb},{"Weight (kg):",wf},{"Owner Name:",of},{"Owner Phone:",pf}};
        for (Object[] r : rows) { form.add(new JLabel((String)r[0])); form.add((Component)r[1]); }
        if (JOptionPane.showConfirmDialog(this, form, "Register New Cat", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                String name = nf.getText().trim();
                if (name.isEmpty()) throw new Exception("Cat name cannot be empty.");
                Cat cat = new Cat(name,
                        bf.getText().trim().isEmpty() ? "Unknown" : bf.getText().trim(),
                        af.getText().trim().isEmpty() ? 0 : Integer.parseInt(af.getText().trim()),
                        (String)gb.getSelectedItem(),
                        wf.getText().trim().isEmpty() ? 0 : Double.parseDouble(wf.getText().trim()),
                        of.getText().trim(), pf.getText().trim());
                cats.add(cat); DataManager.saveData(cats); refreshCats();
                catSelector.setSelectedItem(cat); setStatus("Cat '" + name + "' registered.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,"Enter valid numbers for age/weight.","Error",JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void removeCat() {
        Cat cat = (Cat) catSelector.getSelectedItem();
        if (cat == null) return;
        if (JOptionPane.showConfirmDialog(this,"Remove '"+cat.getName()+"' permanently?",
                "Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            cats.remove(cat); DataManager.saveData(cats); refreshCats(); setStatus("Cat removed.");
        }
    }

    void refreshCats() { catSelector.removeAllItems(); for (Cat c : cats) catSelector.addItem(c); }
    void setStatus(String m) { statusLabel.setText(m); }

    private JButton navButton(String text) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(160,36)); b.setPreferredSize(new Dimension(160,36));
        b.setBackground(new Color(30,55,90)); b.setForeground(Theme.TEXT_MAIN);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(45,80,130)); }
            public void mouseExited(MouseEvent e)  { b.setBackground(new Color(30,55,90)); }
        });
        return b;
    }
}

// ─────────────────────────────────────────────────────────────
//  SYMPTOM SCANNER
// ─────────────────────────────────────────────────────────────

class SymptomScanner extends JDialog {
    private Dashboard parent;
    private Cat cat;
    private JTextField tempField, vetField;
    private JComboBox<String> hydBox, appBox, digBox, behBox;
    private JCheckBox bloodCb, weakCb, seizCb;
    private JTextArea notesArea, resultArea;
    private JLabel resultTitle;

    SymptomScanner(Dashboard parent, Cat cat) {
        super(parent, "Symptom Scanner - " + cat.getName(), true);
        this.parent = parent; this.cat = cat;
        setSize(700, 600); setLocationRelativeTo(parent);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());
        add(Util.topBar("Symptom Scanner", Theme.ACCENT2, "Cat: " + cat.getName() + "  |  " + cat.getBreed()), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel buildContent() {
        JPanel p = new JPanel(new GridLayout(1,2,12,0));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(14,14,8,14));
        p.add(buildForm()); p.add(buildResult());
        return p;
    }

    private JPanel buildForm() {
        JPanel f = new JPanel();
        f.setBackground(Theme.CARD_BG);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50,80,120)),
                BorderFactory.createEmptyBorder(14,14,14,14)));
        f.setLayout(new BoxLayout(f, BoxLayout.Y_AXIS));

        JLabel h = new JLabel("Enter Symptoms");
        h.setFont(new Font("Segoe UI", Font.BOLD, 13)); h.setForeground(Theme.ACCENT);
        h.setAlignmentX(Component.LEFT_ALIGNMENT); f.add(h);
        f.add(Box.createVerticalStrut(10));

        f.add(Theme.dimLabel("Body Temperature (C)  —  Normal: 38.1 to 39.2"));
        tempField = new JTextField("38.5"); Theme.styleField(tempField); f.add(tempField);
        f.add(Box.createVerticalStrut(7));

        f.add(Theme.dimLabel("Hydration Level"));
        hydBox = new JComboBox<>(new String[]{"Good","Mild Dehydration","Severe Dehydration"});
        Theme.styleCombo(hydBox); f.add(hydBox); f.add(Box.createVerticalStrut(7));

        f.add(Theme.dimLabel("Appetite / Eating Habits"));
        appBox = new JComboBox<>(new String[]{"Normal","Reduced","Not Eating"});
        Theme.styleCombo(appBox); f.add(appBox); f.add(Box.createVerticalStrut(7));

        f.add(Theme.dimLabel("Digestion Status"));
        digBox = new JComboBox<>(new String[]{"Normal","Vomiting","Diarrhea","Constipation"});
        Theme.styleCombo(digBox); f.add(digBox); f.add(Box.createVerticalStrut(7));

        f.add(Theme.dimLabel("Behavior Changes"));
        behBox = new JComboBox<>(new String[]{"Normal","Lethargic","Hiding","Aggressive","Howling"});
        Theme.styleCombo(behBox); f.add(behBox); f.add(Box.createVerticalStrut(10));

        JLabel cl = new JLabel("Critical Symptoms (check if present):");
        cl.setFont(new Font("Segoe UI", Font.PLAIN, 11)); cl.setForeground(Theme.WARNING);
        cl.setAlignmentX(Component.LEFT_ALIGNMENT); f.add(cl);
        bloodCb = cb("Blood present (stool / urine / vomit)");
        weakCb  = cb("Extreme weakness / unable to stand");
        seizCb  = cb("Seizures / convulsions");
        f.add(bloodCb); f.add(weakCb); f.add(seizCb);
        f.add(Box.createVerticalStrut(8));

        f.add(Theme.dimLabel("Vet Name (optional)"));
        vetField = new JTextField(); Theme.styleField(vetField); f.add(vetField);
        f.add(Box.createVerticalStrut(7));

        f.add(Theme.dimLabel("Additional Notes"));
        notesArea = new JTextArea(3,1);
        notesArea.setBackground(new Color(20,35,60)); notesArea.setForeground(Theme.TEXT_MAIN);
        notesArea.setFont(new Font("Segoe UI",Font.PLAIN,12));
        notesArea.setBorder(BorderFactory.createLineBorder(new Color(50,80,120)));
        notesArea.setLineWrap(true); notesArea.setWrapStyleWord(true);
        notesArea.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
        f.add(notesArea);
        return f;
    }

    private JPanel buildResult() {
        JPanel r = new JPanel();
        r.setBackground(Theme.CARD_BG);
        r.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50,80,120)),
                BorderFactory.createEmptyBorder(16,14,14,14)));
        r.setLayout(new BoxLayout(r, BoxLayout.Y_AXIS));
        JLabel h = new JLabel("Scan Result");
        h.setFont(new Font("Segoe UI",Font.BOLD,13)); h.setForeground(Theme.ACCENT);
        h.setAlignmentX(Component.LEFT_ALIGNMENT); r.add(h);
        r.add(Box.createVerticalStrut(14));
        resultTitle = new JLabel("Run the scan to see results.", SwingConstants.CENTER);
        resultTitle.setFont(new Font("Georgia",Font.BOLD,14)); resultTitle.setForeground(Theme.TEXT_DIM);
        resultTitle.setAlignmentX(Component.CENTER_ALIGNMENT); r.add(resultTitle);
        r.add(Box.createVerticalStrut(12));
        resultArea = new JTextArea("Fill in symptoms on the left and click 'Run Scan'.");
        resultArea.setEditable(false); resultArea.setLineWrap(true); resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(20,35,60)); resultArea.setForeground(Theme.TEXT_DIM);
        resultArea.setFont(new Font("Segoe UI",Font.PLAIN,12));
        resultArea.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        resultArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        r.add(new JScrollPane(resultArea));
        return r;
    }

    private JPanel buildButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER,12,10));
        bar.setBackground(Theme.BG_DARK);
        bar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(40,65,100)));
        JButton scan = Theme.smallButton("Run Symptom Scan", new Color(30,100,180));
        scan.setFont(new Font("Segoe UI",Font.BOLD,13));
        scan.addActionListener(e -> runScan());
        JButton clear = Theme.smallButton("Clear", new Color(60,70,90));
        clear.addActionListener(e -> clearForm());
        JButton close = Theme.smallButton("Close", new Color(80,30,30));
        close.addActionListener(e -> dispose());
        bar.add(scan); bar.add(clear); bar.add(close);
        return bar;
    }

    private void runScan() {
        try {
            double temp = Double.parseDouble(tempField.getText().trim());
            HealthRecord rec = new HealthRecord(LocalDate.now(), temp,
                    (String)hydBox.getSelectedItem(), (String)appBox.getSelectedItem(),
                    (String)digBox.getSelectedItem(), (String)behBox.getSelectedItem(),
                    bloodCb.isSelected(), weakCb.isSelected(), seizCb.isSelected(),
                    notesArea.getText().trim(), vetField.getText().trim());
            cat.addHealthRecord(rec);
            DataManager.saveData(parent.cats);
            parent.setStatus("Scan saved for " + cat.getName() + ".");
            showResult(rec);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"Enter a valid temperature (e.g. 38.5)","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showResult(HealthRecord r) {
        Color c;
        switch (r.getSeverity()) {
            case EMERGENCY: c = Theme.DANGER;   break;
            case WARNING:   c = Theme.WARNING;  break;
            default:        c = Theme.SUCCESS;  break;
        }
        resultTitle.setText("STATUS: " + r.getSeverityLabel());
        resultTitle.setForeground(c);

        String detail = "Date:       " + r.getFormattedDate() + "\n"
                      + "Temp:       " + r.getTemperature() + "C\n"
                      + "Hydration:  " + r.getHydrationLevel() + "\n"
                      + "Appetite:   " + r.getAppetiteLevel() + "\n"
                      + "Digestion:  " + r.getDigestionStatus() + "\n"
                      + "Behavior:   " + r.getBehaviorChanges() + "\n"
                      + "Blood:      " + (r.isBloodPresent() ? "YES" : "No") + "\n"
                      + "Weakness:   " + (r.isExtremeWeakness() ? "YES" : "No") + "\n"
                      + "Seizures:   " + (r.isSeizures() ? "YES" : "No") + "\n\n"
                      + r.getSeverityAdvice();

        resultArea.setForeground(c);
        resultArea.setText(detail);
        resultArea.setCaretPosition(0);

        if (r.getSeverity() == HealthRecord.Severity.EMERGENCY) {
            JOptionPane.showMessageDialog(this,
                    "EMERGENCY DETECTED!\n\n" + r.getSeverityAdvice() + "\n\nSeek immediate vet care!",
                    "EMERGENCY - " + cat.getName(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        tempField.setText("38.5");
        hydBox.setSelectedIndex(0); appBox.setSelectedIndex(0);
        digBox.setSelectedIndex(0); behBox.setSelectedIndex(0);
        bloodCb.setSelected(false); weakCb.setSelected(false); seizCb.setSelected(false);
        notesArea.setText(""); vetField.setText("");
        resultTitle.setText("Run the scan to see results.");
        resultTitle.setForeground(Theme.TEXT_DIM);
        resultArea.setText("Fill in symptoms on the left and click 'Run Scan'.");
        resultArea.setForeground(Theme.TEXT_DIM);
    }

    private JCheckBox cb(String text) {
        JCheckBox c = new JCheckBox(text);
        c.setBackground(Theme.CARD_BG); c.setForeground(new Color(255,140,140));
        c.setFont(new Font("Segoe UI",Font.PLAIN,11));
        c.setAlignmentX(Component.LEFT_ALIGNMENT); c.setFocusPainted(false);
        return c;
    }
}

// ─────────────────────────────────────────────────────────────
//  HEALTH HISTORY
// ─────────────────────────────────────────────────────────────

class HealthHistory extends JDialog {
    private Cat cat;
    private DefaultTableModel model;
    private JTextArea detail;

    HealthHistory(Dashboard parent, Cat cat) {
        super(parent, "Health History - " + cat.getName(), true);
        this.cat = cat;
        setSize(820,520); setLocationRelativeTo(parent);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());
        add(Util.topBar("Health History", new Color(200,140,255), "Total Records: " + cat.getHealthRecords().size()), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        load(); setVisible(true);
    }

    private JPanel buildContent() {
        JPanel p = new JPanel(new BorderLayout(12,0));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(14,14,8,14));

        String[] cols = {"Date","Temp","Hydration","Appetite","Behavior","Status"};
        model = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        JTable table = new JTable(model);
        table.setBackground(Theme.CARD_BG); table.setForeground(Theme.TEXT_MAIN);
        table.setFont(new Font("Segoe UI",Font.PLAIN,12)); table.setRowHeight(26);
        table.setShowGrid(false); table.setSelectionBackground(new Color(45,80,130));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(Theme.BG_PANEL);
        table.getTableHeader().setForeground(Theme.TEXT_DIM);
        table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int row,int col) {
                super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                setBackground(sel ? new Color(45,80,130) : Theme.CARD_BG);
                setForeground(Theme.TEXT_MAIN);
                if (!sel && row < cat.getHealthRecords().size()) {
                    HealthRecord.Severity s = cat.getHealthRecords().get(row).getSeverity();
                    if (s==HealthRecord.Severity.EMERGENCY) setBackground(new Color(60,20,20));
                    else if (s==HealthRecord.Severity.WARNING) setBackground(new Color(55,42,10));
                }
                setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
                return this;
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showDetail(table.getSelectedRow());
        });

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.CARD_BG);
        sp.setBorder(BorderFactory.createLineBorder(new Color(40,70,110)));
        sp.setPreferredSize(new Dimension(490,0));

        detail = new JTextArea("Select a record to view details.");
        detail.setEditable(false); detail.setLineWrap(true); detail.setWrapStyleWord(true);
        detail.setBackground(Theme.CARD_BG); detail.setForeground(Theme.TEXT_MAIN);
        detail.setFont(new Font("Segoe UI",Font.PLAIN,12));

        JPanel dp = new JPanel(new BorderLayout());
        dp.setBackground(Theme.CARD_BG);
        dp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40,70,110)),
                BorderFactory.createEmptyBorder(12,12,12,12)));
        JLabel dh = new JLabel("Record Detail");
        dh.setFont(new Font("Segoe UI",Font.BOLD,12)); dh.setForeground(Theme.ACCENT);
        dp.add(dh, BorderLayout.NORTH);
        dp.add(new JScrollPane(detail), BorderLayout.CENTER);

        p.add(sp, BorderLayout.CENTER); p.add(dp, BorderLayout.EAST);
        return p;
    }

    private JPanel buildButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        bar.setBackground(Theme.BG_DARK);
        bar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(40,65,100)));
        JButton c = Theme.smallButton("Close", new Color(60,70,90));
        c.addActionListener(e -> dispose());
        bar.add(c); return bar;
    }

    private void load() {
        model.setRowCount(0);
        for (HealthRecord r : cat.getHealthRecords())
            model.addRow(new Object[]{r.getFormattedDate(), r.getTemperature()+"C",
                    r.getHydrationLevel(), r.getAppetiteLevel(),
                    r.getBehaviorChanges(), r.getSeverityLabel()});
    }

    private void showDetail(int row) {
        if (row < 0 || row >= cat.getHealthRecords().size()) return;
        HealthRecord r = cat.getHealthRecords().get(row);
        StringBuilder sb = new StringBuilder();
        sb.append("=== HEALTH RECORD ===\n")
          .append("Date:      ").append(r.getFormattedDate()).append("\n")
          .append("Status:    ").append(r.getSeverityLabel()).append("\n\n")
          .append("Temp:      ").append(r.getTemperature()).append("C\n")
          .append("Hydration: ").append(r.getHydrationLevel()).append("\n")
          .append("Appetite:  ").append(r.getAppetiteLevel()).append("\n")
          .append("Digestion: ").append(r.getDigestionStatus()).append("\n")
          .append("Behavior:  ").append(r.getBehaviorChanges()).append("\n\n")
          .append("Blood:     ").append(r.isBloodPresent() ? "YES" : "No").append("\n")
          .append("Weakness:  ").append(r.isExtremeWeakness() ? "YES" : "No").append("\n")
          .append("Seizures:  ").append(r.isSeizures() ? "YES" : "No").append("\n");
        if (r.getVetName()!=null && !r.getVetName().isEmpty()) sb.append("\nVet: ").append(r.getVetName()).append("\n");
        if (r.getNotes()!=null && !r.getNotes().isEmpty()) sb.append("\nNotes:\n").append(r.getNotes());
        detail.setText(sb.toString()); detail.setCaretPosition(0);
    }
}

// ─────────────────────────────────────────────────────────────
//  EXPENSE TRACKER
// ─────────────────────────────────────────────────────────────

class ExpenseTracker extends JDialog {
    private Dashboard parent;
    private Cat cat;
    private DefaultTableModel model;
    private JLabel totalLbl, paidLbl, pendingLbl;
    private JTable table;

    ExpenseTracker(Dashboard parent, Cat cat) {
        super(parent, "Expense Tracker - " + cat.getName(), true);
        this.parent = parent; this.cat = cat;
        setSize(830,550); setLocationRelativeTo(parent);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());
        add(buildTop(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        load(); setVisible(true);
    }

    private JPanel buildTop() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.BG_PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,new Color(50,80,120)),
                BorderFactory.createEmptyBorder(12,18,12,18)));
        JLabel t = new JLabel("Expense Tracker");
        t.setFont(new Font("Georgia",Font.BOLD,16)); t.setForeground(new Color(120,220,150));
        JPanel sp = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,0));
        sp.setOpaque(false);
        totalLbl   = sl("Total: Rs.0.00",   Theme.ACCENT);
        paidLbl    = sl("Paid: Rs.0.00",    Theme.SUCCESS);
        pendingLbl = sl("Pending: Rs.0.00", Theme.WARNING);
        sp.add(totalLbl); sp.add(paidLbl); sp.add(pendingLbl);
        bar.add(t, BorderLayout.WEST); bar.add(sp, BorderLayout.EAST);
        return bar;
    }

    private JLabel sl(String text, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI",Font.BOLD,12)); l.setForeground(c); return l;
    }

    private JPanel buildContent() {
        JPanel p = new JPanel(new BorderLayout(0,10));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(14,14,8,14));
        p.add(buildForm(), BorderLayout.NORTH);

        String[] cols = {"Date","Category","Description","Clinic","Amount","Due Date","Status"};
        model = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model);
        table.setBackground(Theme.CARD_BG); table.setForeground(Theme.TEXT_MAIN);
        table.setFont(new Font("Segoe UI",Font.PLAIN,12)); table.setRowHeight(26);
        table.setShowGrid(false); table.setSelectionBackground(new Color(45,80,130));
        table.getTableHeader().setBackground(Theme.BG_PANEL);
        table.getTableHeader().setForeground(Theme.TEXT_DIM);
        table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int row,int col) {
                super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                setBackground(sel ? new Color(45,80,130) : Theme.CARD_BG);
                setForeground(Theme.TEXT_MAIN);
                if (!sel && row<cat.getExpenses().size() && !cat.getExpenses().get(row).isPaid())
                    setBackground(new Color(50,40,15));
                setBorder(BorderFactory.createEmptyBorder(0,8,0,8));
                return this;
            }
        });
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2) toggle(table.getSelectedRow());
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.CARD_BG);
        sp.setBorder(BorderFactory.createLineBorder(new Color(40,70,110)));

        JLabel hint = new JLabel("  Double-click a row to toggle Paid / Pending");
        hint.setFont(new Font("Segoe UI",Font.ITALIC,11)); hint.setForeground(Theme.TEXT_DIM);

        p.add(sp, BorderLayout.CENTER); p.add(hint, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildForm() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        f.setBackground(Theme.CARD_BG);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50,80,120)),
                BorderFactory.createEmptyBorder(8,10,8,10)));

        JComboBox<Expense.Category> catBox = new JComboBox<>(Expense.Category.values());
        catBox.setBackground(new Color(20,35,60)); catBox.setForeground(Theme.TEXT_MAIN);
        catBox.setFont(new Font("Segoe UI",Font.PLAIN,11));
        catBox.setPreferredSize(new Dimension(130,26));

        JTextField desc  = sf("Description",12);
        JTextField amt   = sf("Amount",7);
        JTextField clinic= sf("Clinic",10);
        JTextField due   = sf("Due YYYY-MM-DD",10);

        JCheckBox paid = new JCheckBox("Paid");
        paid.setBackground(Theme.CARD_BG); paid.setForeground(Theme.SUCCESS);
        paid.setFont(new Font("Segoe UI",Font.PLAIN,11)); paid.setFocusPainted(false);

        JButton add = Theme.smallButton("+ Add Expense", new Color(40,120,60));
        add.addActionListener(e -> {
            try {
                if (desc.getText().trim().isEmpty()) throw new Exception("Description required.");
                double a = Double.parseDouble(amt.getText().trim());
                LocalDate d = due.getText().trim().isEmpty() ? null : LocalDate.parse(due.getText().trim());
                cat.addExpense(new Expense(LocalDate.now(),(Expense.Category)catBox.getSelectedItem(),
                        desc.getText().trim(),a,clinic.getText().trim(),paid.isSelected(),d));
                DataManager.saveData(parent.cats); load();
                desc.setText(""); amt.setText(""); clinic.setText(""); due.setText(""); paid.setSelected(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,"Enter a valid amount.","Error",JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });

        f.add(new JLabel("Category:")); f.add(catBox);
        f.add(desc); f.add(amt); f.add(clinic); f.add(due); f.add(paid); f.add(add);
        return f;
    }

    private JPanel buildButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        bar.setBackground(Theme.BG_DARK);
        bar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(40,65,100)));
        JButton c = Theme.smallButton("Close", new Color(60,70,90));
        c.addActionListener(e -> dispose());
        bar.add(c); return bar;
    }

    private void load() {
        model.setRowCount(0);
        double total=0, paid=0, pending=0;
        for (Expense e : cat.getExpenses()) {
            model.addRow(new Object[]{e.getFormattedDate(),e.getCategory().getLabel(),e.getDescription(),
                    e.getVetClinic().isEmpty()?"-":e.getVetClinic(),
                    String.format("Rs.%.2f",e.getAmount()),e.getFormattedDueDate(),
                    e.isPaid()?"Paid":"Pending"});
            total+=e.getAmount();
            if(e.isPaid()) paid+=e.getAmount(); else pending+=e.getAmount();
        }
        totalLbl.setText(String.format("Total: Rs.%.2f",total));
        paidLbl.setText(String.format("Paid: Rs.%.2f",paid));
        pendingLbl.setText(String.format("Pending: Rs.%.2f",pending));
    }

    private void toggle(int row) {
        if (row<0||row>=cat.getExpenses().size()) return;
        Expense e = cat.getExpenses().get(row);
        e.setPaid(!e.isPaid());
        DataManager.saveData(parent.cats); load();
    }

    private JTextField sf(String tip, int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(new Color(20,35,60)); f.setForeground(Theme.TEXT_MAIN);
        f.setFont(new Font("Segoe UI",Font.PLAIN,11));
        f.setBorder(BorderFactory.createLineBorder(new Color(50,80,120)));
        f.setToolTipText(tip); f.setCaretColor(Theme.ACCENT);
        f.setPreferredSize(new Dimension(f.getPreferredSize().width,26));
        return f;
    }
}

// ─────────────────────────────────────────────────────────────
//  EMERGENCY GUIDE
// ─────────────────────────────────────────────────────────────

class EmergencyGuide extends JDialog {
    EmergencyGuide(Dashboard parent) {
        super(parent, "Emergency Guide & First Aid", true);
        setSize(660,550); setLocationRelativeTo(parent);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(70,15,15));
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,Theme.DANGER),
                BorderFactory.createEmptyBorder(12,18,12,18)));
        JLabel t = new JLabel("EMERGENCY GUIDE & FIRST AID");
        t.setFont(new Font("Georgia",Font.BOLD,16)); t.setForeground(Theme.DANGER);
        topBar.add(t, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        JPanel guide = new JPanel();
        guide.setBackground(Theme.BG_DARK);
        guide.setLayout(new BoxLayout(guide, BoxLayout.Y_AXIS));

        guide.add(section("SIGNS REQUIRING IMMEDIATE VET", Theme.DANGER, new String[]{
            "Blood in urine, stool, or vomit — possible internal bleeding.",
            "Extreme weakness or inability to stand — possible organ failure.",
            "Seizures or convulsions — neurological emergency.",
            "Temperature below 36C or above 41C.",
            "Breathing difficulty or open-mouth breathing.",
            "Severe dehydration — skin does not snap back when pinched.",
            "Unconsciousness or unresponsiveness.",
            "Suspected poisoning (plants, chemicals, medications)."}));

        guide.add(Box.createVerticalStrut(8));
        guide.add(section("WARNING SIGNS — VET WITHIN 24-48 HOURS", Theme.WARNING, new String[]{
            "Not eating for more than 24 hours.",
            "Persistent vomiting or ongoing diarrhea.",
            "Hiding continuously and avoiding contact.",
            "Excessive howling or unusual vocalizations.",
            "Sudden aggression or personality change.",
            "Rapid or uncontrolled weight loss.",
            "Excessive drinking or urination."}));

        guide.add(Box.createVerticalStrut(8));
        guide.add(section("BASIC FIRST AID TIPS", Theme.ACCENT2, new String[]{
            "Keep the cat calm and warm. Wrap gently in a clean towel.",
            "Do NOT give human medications — they are toxic to cats.",
            "For bleeding: apply gentle pressure with a clean cloth.",
            "For poisoning: do NOT induce vomiting. Call vet immediately.",
            "For seizures: do not restrain the cat. Clear nearby sharp objects.",
            "For heatstroke: move to cool area, apply cool water, rush to vet."}));

        guide.add(Box.createVerticalStrut(8));
        guide.add(section("NORMAL CAT HEALTH REFERENCE", Theme.SUCCESS, new String[]{
            "Body Temperature:  38.1C to 39.2C  (100.5F to 102.5F)",
            "Heart Rate:        120 to 140 beats per minute",
            "Respiratory Rate:  20 to 30 breaths per minute",
            "Hydration:         Skin snaps back within 1-2 seconds",
            "Urination:         2 to 4 times per day is normal"}));

        JScrollPane scroll = new JScrollPane(guide);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(scroll, BorderLayout.CENTER);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        bot.setBackground(Theme.BG_DARK);
        bot.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(40,65,100)));
        JButton cl = Theme.smallButton("Close", new Color(60,70,90));
        cl.addActionListener(e -> dispose()); bot.add(cl);
        add(bot, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel section(String heading, Color color, String[] points) {
        JPanel s = new JPanel();
        s.setBackground(Theme.CARD_BG);
        s.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50,70,100)),
                BorderFactory.createEmptyBorder(10,14,10,14)));
        s.setLayout(new BoxLayout(s, BoxLayout.Y_AXIS));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        JLabel h = new JLabel(heading);
        h.setFont(new Font("Segoe UI",Font.BOLD,12)); h.setForeground(color);
        h.setAlignmentX(Component.LEFT_ALIGNMENT); s.add(h);
        s.add(Box.createVerticalStrut(6));
        for (String pt : points) {
            JLabel l = new JLabel("<html><body style='width:560px'>• " + pt + "</body></html>");
            l.setFont(new Font("Segoe UI",Font.PLAIN,12)); l.setForeground(Theme.TEXT_MAIN);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            l.setBorder(BorderFactory.createEmptyBorder(2,0,2,0));
            s.add(l);
        }
        return s;
    }
}

// ─────────────────────────────────────────────────────────────
//  CAT PROFILE
// ─────────────────────────────────────────────────────────────

class CatProfile extends JDialog {
    private Dashboard parent;
    private Cat cat;
    private JTextField nf, bf, af, wf, of, pf;
    private JComboBox<String> gb;

    CatProfile(Dashboard parent, Cat cat) {
        super(parent, "Cat Profile - " + cat.getName(), true);
        this.parent = parent; this.cat = cat;
        setSize(480,470); setLocationRelativeTo(parent);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());
        add(Util.topBar("Cat Profile", Theme.ACCENT, ""), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel buildContent() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(Theme.BG_DARK);
        outer.setBorder(BorderFactory.createEmptyBorder(20,30,10,30));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.CARD_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50,80,120)),
                BorderFactory.createEmptyBorder(20,24,20,24)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6,6,6,6);

        nf = mf(cat.getName()); bf = mf(cat.getBreed());
        af = mf(String.valueOf(cat.getAge())); wf = mf(String.valueOf(cat.getWeight()));
        of = mf(cat.getOwnerName()); pf = mf(cat.getOwnerPhone());
        gb = new JComboBox<>(new String[]{"Male","Female"});
        gb.setSelectedItem(cat.getGender());
        gb.setBackground(new Color(20,35,60)); gb.setForeground(Theme.TEXT_MAIN);
        gb.setFont(new Font("Segoe UI",Font.PLAIN,12));

        String[] labels = {"Cat Name:","Breed:","Age (months):","Gender:","Weight (kg):","Owner Name:","Owner Phone:"};
        Component[] fields = {nf,bf,af,gb,wf,of,pf};
        for (int i=0; i<labels.length; i++) {
            g.gridx=0; g.gridy=i; g.weightx=0.3;
            JLabel l = new JLabel(labels[i]);
            l.setFont(new Font("Segoe UI",Font.BOLD,12)); l.setForeground(Theme.TEXT_DIM);
            form.add(l,g);
            g.gridx=1; g.weightx=0.7; form.add(fields[i],g);
        }
        g.gridx=0; g.gridy=labels.length; g.gridwidth=2;
        g.insets=new Insets(14,6,4,6);
        JLabel st = new JLabel("Medical Summary");
        st.setFont(new Font("Segoe UI",Font.BOLD,11)); st.setForeground(Theme.ACCENT2);
        form.add(st,g);
        g.gridy++; g.insets=new Insets(4,6,4,6);
        long em = cat.getHealthRecords().stream().filter(r->r.getSeverity()==HealthRecord.Severity.EMERGENCY).count();
        JLabel stats = new JLabel("<html><body style='width:280px'>"
                + "Total health records: <b>"+cat.getHealthRecords().size()+"</b><br>"
                + "Emergency events: <b>"+em+"</b><br>"
                + "Total vet expenses: <b>Rs."+String.format("%.2f",cat.getTotalExpenses())+"</b>"
                + "</body></html>");
        stats.setFont(new Font("Segoe UI",Font.PLAIN,12)); stats.setForeground(Theme.TEXT_MAIN);
        form.add(stats,g);

        outer.add(form, BorderLayout.CENTER); return outer;
    }

    private JPanel buildButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER,12,10));
        bar.setBackground(Theme.BG_DARK);
        bar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(40,65,100)));
        JButton sv = Theme.smallButton("Save Changes", new Color(30,100,60));
        sv.addActionListener(e -> save());
        JButton cl = Theme.smallButton("Close", new Color(60,70,90));
        cl.addActionListener(e -> dispose());
        bar.add(sv); bar.add(cl); return bar;
    }

    private void save() {
        try {
            String name = nf.getText().trim();
            if (name.isEmpty()) throw new Exception("Cat name cannot be empty.");
            cat.setName(name); cat.setBreed(bf.getText().trim());
            cat.setAge(Integer.parseInt(af.getText().trim()));
            cat.setGender((String)gb.getSelectedItem());
            cat.setWeight(Double.parseDouble(wf.getText().trim()));
            cat.setOwnerName(of.getText().trim()); cat.setOwnerPhone(pf.getText().trim());
            DataManager.saveData(parent.cats); parent.refreshCats();
            parent.setStatus("Profile updated for '" + name + "'.");
            JOptionPane.showMessageDialog(this,"Profile saved successfully!","Saved",JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"Enter valid numbers for age/weight.","Error",JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField mf(String val) {
        JTextField f = new JTextField(val);
        f.setBackground(new Color(20,35,60)); f.setForeground(Theme.TEXT_MAIN);
        f.setFont(new Font("Segoe UI",Font.PLAIN,12));
        f.setBorder(BorderFactory.createLineBorder(new Color(50,80,120)));
        f.setCaretColor(Theme.ACCENT); return f;
    }
}

// ─────────────────────────────────────────────────────────────
//  SHARED UTILITY — topBar used by all dialogs
// ─────────────────────────────────────────────────────────────

class Util {
    static JPanel topBar(String title, Color titleColor, String right) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.BG_PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,new Color(50,80,120)),
                BorderFactory.createEmptyBorder(12,18,12,18)));
        JLabel t = new JLabel(title);
        t.setFont(new Font("Georgia",Font.BOLD,16)); t.setForeground(titleColor);
        bar.add(t, BorderLayout.WEST);
        if (!right.isEmpty()) {
            JLabel r = new JLabel(right);
            r.setFont(new Font("Segoe UI",Font.PLAIN,12)); r.setForeground(Theme.TEXT_DIM);
            bar.add(r, BorderLayout.EAST);
        }
        return bar;
    }
}

// helper so inner classes can call topBar without full qualifier
// Add this helper method at the end — included directly in each dialog class for simplicity