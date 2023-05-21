import java.util.Scanner;

class Chat {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String statement = "say Hello";

        String[] repliesToLegalRequest = {"say " +  ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + "? okay: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                "you want me to say " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + " do you? alright: " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER};
        String[] repliesToIllegalRequest = {"what ", "say i should say "};

        ChatterBot[] bots = new ChatterBot[2];
        bots[0] = new ChatterBot("Hans",repliesToLegalRequest,repliesToIllegalRequest);

        repliesToIllegalRequest[0] = "whaaat ";
        repliesToIllegalRequest[1] = "say say ";
        bots[1] = new ChatterBot("Rudolf",repliesToLegalRequest,repliesToIllegalRequest);

        while(true) {
            for(ChatterBot bot : bots) {
                statement = bot.replyTo(statement);
                System.out.print(bot.getName() + ": " + statement);
                scanner.nextLine(); //wait for “enter” before continuing
            }
        }
    }
}
