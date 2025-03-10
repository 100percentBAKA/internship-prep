import { Component, Input } from '@angular/core';
import { Product } from '../../pages/product-list/product-list.component';
import { PrimaryButtonComponent } from '../../shared/primary-button/primary-button.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product-card',
  imports: [PrimaryButtonComponent, CommonModule],
  templateUrl: './product-card.component.html',
  styleUrl: './product-card.component.css',
})
export class ProductCardComponent {
  @Input() productInfo: Product = {
    id: 1,
    title: 'Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops',
    price: 109.95,
    image: 'https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg',
    stock: 10,
  };
}
