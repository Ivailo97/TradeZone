import { Component, OnInit, Input } from '@angular/core';
import { StompService } from 'ng2-stomp-service';
import { MessageService } from 'src/app/core/services/message.service';
import { ChanelService } from 'src/app/core/services/chanel.service';
import { Message } from '../messages-modal/messages-modal.component';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-conversation-modal',
  templateUrl: './conversation-modal.component.html',
  styleUrls: ['./conversation-modal.component.css']
})
export class ConversationModalComponent implements OnInit {

  form: FormGroup;

  filteredMessages: Array<Message> = [];

  channel: string;

  @Input()
  username: string;

  constructor(private stompService: StompService,
    private messageService: MessageService,
    private channelService: ChanelService,
    private fb: FormBuilder) { }

  ngOnInit() {
    this.form = this.fb.group({
      message: ['', [Validators.required]],
    })

    this.channelService.getChannel().subscribe(channel => {
      this.channel = channel;
      this.filterMessages();
    });

    this.messageService.getMessages().subscribe(messages => {
      this.filteredMessages = messages;
    });
  }

  sendMessage() {
    if (this.f.message.value) {
      this.stompService.send('/app/messages', {
        'channel': this.channel,
        'sender': this.username,
        'content': this.f.message.value
      });
      this.f.message.setValue('');
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

  get f() {
    return this.form.controls;
  }
}
