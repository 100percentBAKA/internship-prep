import { Component } from '@angular/core';
import { PrimaryButtonComponent } from '../../shared/primary-button/primary-button.component';

@Component({
  selector: 'app-header',
  imports: [PrimaryButtonComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {}
