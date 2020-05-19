import { Component, OnInit, Input } from '@angular/core';
import { StompService } from 'ng2-stomp-service';
import { MessageService } from 'src/app/core/services/message.service';
import { ChanelService } from 'src/app/core/services/chanel.service';
import { Message } from '../messages-modal/messages-modal.component';

@Component({
  selector: 'app-conversation-modal',
  templateUrl: './conversation-modal.component.html',
  styleUrls: ['./conversation-modal.component.css']
})
export class ConversationModalComponent implements OnInit {

  filteredMessages: Array<Message> = [];

  messageToSend:string;

  channel: string;

  @Input()
  username: string;

  constructor(private stompService: StompService,
    private messageService: MessageService,
    private channelService: ChanelService) { }

  ngOnInit() {

    this.channelService.getChannel().subscribe(channel => {
      this.channel = channel;
      this.filterMessages();
    });

    this.messageService.getMessages().subscribe(messages => {
      this.filteredMessages = messages;
    });
  }

  sendMessage() {
    if (this.messageToSend) {
      this.stompService.send('/app/messages', {
        'channel': this.channel,
        'sender': this.username,
        'content': this.messageToSend
      });

      this.messageToSend = '';
      this.scrollToBottom();
    }
  }

  filterMessages() {
    this.messageService.filterMessages(this.channel);
    this.scrollToBottom();
  }

  scrollToBottom() {
    const msgContainer = document.getElementById('msg-container');
    msgContainer.scrollTop =msgContainer.scrollHeight - msgContainer.clientHeight;
  }
}
