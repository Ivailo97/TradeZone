import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';
import { Message } from 'src/app/core/models/message';
import { AlertService } from '../../alert';
import { ProfileService } from 'src/app/core/services/profile.service';

const alertConfig = {
  autoClose: true
}
const messageSent = 'Message sent!';

@Component({
  selector: 'app-send-message-modal',
  templateUrl: './send-message-modal.component.html',
  styleUrls: ['./send-message-modal.component.css']
})
export class SendMessageModalComponent implements OnInit {

  @Input() receiver: string;

  form: FormGroup

  constructor(private formBuilder: FormBuilder,
    private tokenService: TokenStorageService,
    private alertService: AlertService,
    private profileService: ProfileService) { }

  ngOnInit(): void {

    this.form = this.formBuilder.group({
      content: ['', Validators.required],
      sender: ['', Validators.required],
      receiver: ['', Validators.required]
    })

    this.patchForm();
  }

  sendMessage(): void {

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

  }

  resetForm(): void {
    this.form.reset();
    this.patchForm();
  }

  private get f(): any {
    return this.form.controls;
  }

  private patchForm(): void {
    this.form.patchValue({ sender: this.tokenService.getUsername() });
    this.form.patchValue({ receiver: this.receiver });
  }
}
