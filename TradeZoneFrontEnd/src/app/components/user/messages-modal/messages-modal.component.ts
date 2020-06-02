import { Component, OnInit, Input, HostListener, ViewChild } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { StompService } from 'ng2-stomp-service';
import { ChanelService } from 'src/app/core/services/chanel.service';
import { MessageService } from 'src/app/core/services/message.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConversationModalComponent } from '../conversation-modal/conversation-modal.component';
import { ProfileService } from 'src/app/core/services/profile.service';
import { UserProfile } from 'src/app/core/models/user-profile';

export interface Message {
  channelId: string;
  sender: string;
  content: string;
  timestamp?: number;
  readDate?: number;
  senderAvatarUrl?: string;
}

export interface ConversationUser {
  id: number;
  userUsername: string;
  firstName: string;
  lastName: string;
  connected: boolean;
  photoUrl: string;
}

const stompServiceConfig = { host: "http://localhost:8080/wechat", queue: { init: false } };

@Component({
  selector: 'app-messages-modal',
  templateUrl: './messages-modal.component.html',
  styleUrls: ['./messages-modal.component.css']
})
export class MessagesModalComponent implements OnInit {

  @ViewChild(ConversationModalComponent) conversation: ConversationModalComponent

  NEW_USER_LIFETIME: number = 1000 * 5;

  searchedUser: string;

  @Input()
  username: string;

  users: Array<ConversationUser> = [];
  highlightedUsers: Array<string> = [];
  newConnectedUsers: Array<string> = [];
  channel: string;
  receiver: ConversationUser;
  profile: UserProfile;

  constructor(private authService: AuthService,
    private stompService: StompService,
    private channelService: ChanelService,
    private snackBar: MatSnackBar,
    private messageService: MessageService,
    private profileService: ProfileService) {
  }

  ngOnInit(): void {
    this.initUsers();
    this.initUserEvents();
    this.channelService.getChannel().subscribe(channel => this.channel = channel);
    this.profileService.refreshNeeded$.subscribe(x => this.loadProfile())
    this.loadProfile();
  }

  private loadProfile() {
    this.profileService.getUserProfile().subscribe(profile => this.profile = profile);
  }

  private initUsers(): void {
    this.authService.findUsers()
      .subscribe(
        res => {
          this.users = res;
        }
      );
  }

  searchUser(): void {
    this.authService.findUsers()
      .subscribe(
        res => {
          if (this.searchedUser) {
            this.users = res.filter(x => this.fullNameIncludes(x));;
          } else {
            this.users = res;
          }
        }
      );
  }

  getOtherUsers(): Array<ConversationUser> {
    return this.users.filter(user => user.userUsername !== this.username);
  }

  @HostListener('window:focus', [])
  sendReadReceipt() {
    if (this.channel != null && this.receiver && this.receiver != null) {
      this.messageService.sendReadReceipt(this.channel, this.receiver.userUsername);
    }
  }

  startChatWithUser(user: ConversationUser) {

    const channelId = ChanelService.createChannel(this.username, user.userUsername);
    this.channelService.refreshChannel(channelId);

    if (!this.profile.subscribedTo.includes(channelId)) {
      this.channelService.createChannel(channelId).subscribe(
        success => {
          this.receiver = user
          this.highlightedUsers = this.highlightedUsers.filter(u => u !== user.userUsername);
          this.messageService.sendReadReceipt(channelId, user.userUsername);
        }, error => {
          console.log(error);
        })
    }

    this.receiver = user
    this.highlightedUsers = this.highlightedUsers.filter(u => u !== user.userUsername);
    this.messageService.sendReadReceipt(channelId, user.userUsername);
  }

  getUserItemClass(user: ConversationUser): string {
    let classes: string = 'user-item';
    if (this.receiver && user.userUsername === this.receiver.userUsername) {
      classes += ' current-chat-user';
    }

    if (this.highlightedUsers.indexOf(user.userUsername) >= 0) {
      classes += ' new-message';
    }

    if (this.newConnectedUsers.indexOf(user.userUsername) >= 0) {
      classes += ' new-user';
    }

    return classes;
  }

  initUserEvents() {
    this.stompService.configure(stompServiceConfig)
    this.stompService.startConnect().then(
      () => {
        this.stompService.done('init');
        this.stompService.subscribe('/channel/login', res => {
          if (res.userUsername !== this.username) {
            this.newConnectedUsers.push(res.userUsername);
            setTimeout((
              function () {
                this.removeNewUserBackground(res.userUsername);
              }
            ).bind(this), this.NEW_USER_LIFETIME);
            this.users = this.users.filter(item => item.userUsername !== res.userUsername);
            this.users.push(res);
            this.updateConversationReceiver();
            const channelId = ChanelService.createChannel(this.username, res.userUsername);
            if (!this.channel && this.profile.subscribedTo.includes(channelId)) {
              this.channelService.refreshChannel(channelId);
            }
            if (!this.channel && !this.profile.subscribedTo.includes(channelId)) {
              this.subscribeToOtherUser(res);
            }
          }
        });

        this.stompService.subscribe('/channel/logout', res => {
          this.users = this.users.filter(item => item.userUsername !== res.userUsername);
          this.users.push(res);
          this.updateConversationReceiver()
          // const channelId = ChanelService.createChannel(this.username, res.userUsername);
          // if (this.channel === channelId) {
          //   this.channelService.removeChannel();
          // }
        });

        this.subscribeToOtherUsers(this.users, this.username);
      });
  }

  removeNewUserBackground(username) {
    this.newConnectedUsers = this.newConnectedUsers.filter(u => u !== username);
  }

  subscribeToOtherUsers(users, username) {
    const filteredUsers: Array<any> = users.filter(user => {
      const channelId = ChanelService.createChannel(this.username, user.userUsername);
      return username !== user.userUsername && channelId != this.channel;
    });

    filteredUsers.forEach(user => this.subscribeToOtherUser(user));
  }

  subscribeToOtherUser(otherUser) {

    const channelId = ChanelService.createChannel(this.username, otherUser.userUsername);

    if (channelId === this.channel) {
      return;
    }

    this.stompService.subscribe('/channel/chat/' + channelId, res => {

      this.messageService.pushMessage(res);

      if (!this.profile.subscribedTo.includes(channelId)) {

        this.channelService.subscribeToChannel(this.profile.userUsername, channelId)
          .subscribe(data => {
            if (res.channelId !== this.channel) {
              this.showNotification(res);
            } else {
              this.messageService.sendReadReceipt(this.channel, otherUser.userUsername);
            }
          });
      }
    });

  }

  showNotification(message: Message) {
    const snackBarRef = this.snackBar.open('New message from ' + message.sender, 'Show', { duration: 3000 });
    this.highlightedUsers.push(message.sender);
    snackBarRef.onAction().subscribe(() => {
      this.receiver = this.users.find(x => x.userUsername === message.sender);
      this.channel = ChanelService.createChannel(this.username, message.sender);
      this.channelService.refreshChannel(this.channel);
    });
  }

  private fullNameIncludes(user: ConversationUser): boolean {
    return user.firstName.concat(` ${user.lastName}`).toLocaleLowerCase().includes(this.searchedUser.toLocaleLowerCase().trim())
  }

  private updateConversationReceiver() {
    this.conversation.receiver = this.users.find(x => x.userUsername === this.conversation.receiver.userUsername)
  }
}
