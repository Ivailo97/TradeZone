import { Component, OnInit, Input } from '@angular/core';
import { Conversation } from 'src/app/core/models/conversation';


@Component({
  selector: 'app-messages-modal',
  templateUrl: './messages-modal.component.html',
  styleUrls: ['./messages-modal.component.css']
})
export class MessagesModalComponent implements OnInit {

  @Input() conversations: Array<Conversation>;

  constructor() { }

  ngOnInit(): void {
  }

}
