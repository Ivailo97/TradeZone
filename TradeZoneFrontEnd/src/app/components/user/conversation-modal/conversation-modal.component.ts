import { Component, OnInit, Input } from '@angular/core';
import { Conversation } from 'src/app/core/models/conversation';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ProfileService } from 'src/app/core/services/profile.service';
import { AlertService } from '../../alert';
import { Message } from 'src/app/core/models/message';

const alertConfig = {
  autoClose: true
}
const messageSent = 'Message sent!';

@Component({
  selector: 'app-conversation-modal',
  templateUrl: './conversation-modal.component.html',
  styleUrls: ['./conversation-modal.component.css']
})
export class ConversationModalComponent implements OnInit {

  @Input() conversation: Conversation

  form: FormGroup

  constructor(private formBuilder: FormBuilder,
    private profileService: ProfileService,
    private alertService: AlertService) { }

  ngOnInit(): void {

    this.form = this.formBuilder.group({
      content: ['', Validators.required],
      sender: ['', Validators.required],
      receiver: ['', Validators.required]
    })

    this.patchForm();
  }

  send(): void {

    const message = new Message(
      this.f.content.value,
      this.f.sender.value,
      this.f.receiver.value
    )

    this.profileService.sendMessage(message)
      .subscribe(
        success => {
          this.alertService.success(messageSent, alertConfig)
        },
        fail => {
          this.alertService.error(fail.error.message, alertConfig);
        });

    this.form.reset();
    this.patchForm();
  }

  private patchForm(): void {
    this.form.patchValue({ sender: this.conversation.host.userUsername });
    this.form.patchValue({ receiver: this.conversation.interlocutor.userUsername });
  }

  private get f(): any {
    return this.form.controls;
  }

}
