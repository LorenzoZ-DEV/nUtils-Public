package me.nutilsv3.storage.report;

import me.nutilsv3.Main;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReportStorage {
    private static final String FILE_PATH = "plugins/nutilsv3/reports.csv";


    public static void initialize() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println("ID,Reporter,Reported,Reason,Server,Status,Timestamp");
                pw.close();

                Main.getInstance().getLogger().info("✅ CSV file created successfully!");

            } catch (IOException e) {
                Main.getInstance().getLogger().error("❌ Failed to create CSV file!", e);
            }
        }
    }


    public static int saveReport(String reporter, String reported, String reason, String server) {
        try {
            File file = new File(FILE_PATH);
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            int reportId = getLastReportId() + 1;
            String timestamp = String.valueOf(System.currentTimeMillis());

            pw.println(reportId + "," + reporter + "," + reported + "," + reason + "," + server + ",OPEN," + timestamp);
            pw.close();

            Main.getInstance().getLogger().info("✅ Report saved in CSV successfully!");

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to save report in CSV!", e);
        }
        return 0;
    }


    public static List<String> getOpenReports() {
        List<String> reports = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[5].equals("OPEN")) {
                    reports.add("ID: " + parts[0] + " | Reporter: " + parts[1] + " | Reported: " + parts[2] +
                            " | Reason: " + parts[3] + " | Server: " + parts[4]);
                }
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to read open reports from CSV!", e);
        }

        return reports;
    }


    private static int getLastReportId() {
        int lastId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        if (id > lastId) {
                            lastId = id;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to get last report ID from CSV!", e);
        }

        return lastId;
    }


    public static boolean closeReport(int id) {
        List<String> reports = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && Integer.parseInt(parts[0]) == id) {
                    parts[5] = "CLOSED";
                    found = true;
                }
                reports.add(String.join(",", parts));
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to read reports from CSV!", e);
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String report : reports) {
                    bw.write(report + "\n");
                }
                return true;
            } catch (IOException e) {
                Main.getInstance().getLogger().error("❌ Failed to update reports CSV!", e);
            }
        }
        return false;
    }

    public static int getOpenReportsCount() {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[5].equals("OPEN")) {
                    count++;
                }
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to count open reports from CSV!", e);
        }

        return count;
    }


    public static int getHandledReportsCount(String staffName) {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[6].equalsIgnoreCase(staffName)) {
                    count++;
                }
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to count handled reports from CSV!", e);
        }

        return count;
    }


    public static boolean reopenReport(int id) {
        return updateReportStatus(id, "OPEN");
    }


    public static boolean closeReport(int id, String staffName) {
        return updateReportStatus(id, "CLOSED");
    }


    private static boolean updateReportStatus(int id, String newStatus) {
        List<String> reports = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean firstLine = true; // ✅ Variabile per saltare l'intestazione

            while ((line = br.readLine()) != null) {
                if (firstLine) { // ✅ Salta l'intestazione del CSV
                    firstLine = false;
                    reports.add(line); // ✅ Mantiene l'intestazione nel file
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    try {
                        int reportId = Integer.parseInt(parts[0]); // ✅ Converte solo se non è l'intestazione
                        if (reportId == id) {
                            parts[5] = newStatus; // Cambia lo stato del report
                            found = true;
                        }
                    } catch (NumberFormatException e) {
                        Main.getInstance().getLogger().error("❌ Error parsing report ID: " + parts[0]);
                    }
                }
                reports.add(String.join(",", parts));
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to read reports from CSV!", e);
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String report : reports) {
                    bw.write(report + "\n");
                }
                return true;
            } catch (IOException e) {
                Main.getInstance().getLogger().error("❌ Failed to update reports CSV!", e);
            }
        }
        return false;
    }

    public static boolean assignReport(int id, String staffName) {
        List<String> reports = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && Integer.parseInt(parts[0]) == id) {
                    parts[6] = staffName;
                    found = true;
                }
                reports.add(String.join(",", parts));
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to read reports from CSV!", e);
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String report : reports) {
                    bw.write(report + "\n");
                }
                return true;
            } catch (IOException e) {
                Main.getInstance().getLogger().error("❌ Failed to update reports CSV!", e);
            }
        }
        return false;
    }

    public static String getReportedPlayer(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { // ✅ Salta l'intestazione
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        if (Integer.parseInt(parts[0]) == id) {
                            return parts[2]; // Nome del giocatore segnalato
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to get reported player from CSV!", e);
        }
        return null;
    }



    public static String getReportedServer(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) { // ✅ Salta l'intestazione
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        if (Integer.parseInt(parts[0]) == id) {
                            return parts[4]; // Nome del server
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }

        } catch (IOException e) {
            Main.getInstance().getLogger().error("❌ Failed to get reported server from CSV!", e);
        }
        return null;
    }


}
