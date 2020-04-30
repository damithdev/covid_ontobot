package core.chatbot;

import core.model.ResponseModel;
import core.ontology.OntologyReader;

import java.io.File;
import java.util.Scanner;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;
import org.apache.jena.query.ResultSetFormatter;
import org.javatuples.Pair;

public class Main {

    private static final boolean TRACE_MODE = false;
    private static final String botName = "ontobot";
    private static final String botPreviewName = getCamelCaseString(botName);
    private static final OntologyReader reader = new OntologyReader();

    public static void main(String[] args) {
        try {

            String resourcesPath = getResourcesPath();
            System.out.println(resourcesPath);
            MagicBooleans.trace_mode = TRACE_MODE;
            Bot bot = new Bot(botName, resourcesPath);
            bot.writeAIMLFiles();
            Chat chatSession = new Chat(bot);
            bot.brain.nodeStats();
            String textLine = "";

            while(true) {
                System.out.print("Human : ");
                textLine = IOUtils.readInputTextLine();
                if ((textLine == null) || (textLine.length() < 1))
                    textLine = MagicStrings.null_input;
                if (textLine.equals("q")) {
                    System.exit(0);
                } else if (textLine.equals("wq")) {
                    bot.writeQuit();
                    System.exit(0);
                } else {
                    String request = textLine;
                    if (MagicBooleans.trace_mode)
                        System.out.println("STATE=" + request + ":THAT=" +
                                ((History) chatSession.thatHistory.get(0)).get(0) +
                                ":TOPIC=" + chatSession.predicates.get("topic"));
                    String response = chatSession.multisentenceRespond(request);
                    while (response.contains("&lt;"))
                        response = response.replace("&lt;", "<");
                    while (response.contains("&gt;"))
                        response = response.replace("&gt;", ">");
                    if(response.contains("RT|T")){
                        try{

                            String[] arrOfStr = response.split("\\|");
                            reader.run(arrOfStr,botPreviewName);


                        }catch (Exception ex){
                            response = "Sorry! I am unable to understand";
                            System.out.println(botPreviewName+": " + response);
                            System.out.println("Ex:" + ex.getLocalizedMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }

    private static String getCamelCaseString(String line){
        String upper_case_line = "";
        Scanner lineScan = new Scanner(line);
        while(lineScan.hasNext()) {
            String word = lineScan.next();
            upper_case_line += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
        }
        return upper_case_line;
    }

}
