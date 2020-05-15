import { ConversationMessage } from "./conversation-message";
import { ProfileConversation } from "./profile-conversation";

export class Conversation {

    interlocutor: ProfileConversation;

    messages: Array<ConversationMessage>;

    host: ProfileConversation;
}