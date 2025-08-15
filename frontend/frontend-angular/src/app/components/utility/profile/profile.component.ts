import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { CommonModule } from '@angular/common';
import { User } from '../../../models/user';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MatIconModule, MatFormFieldModule, MatInputModule],
  templateUrl: './profile.component.html',
  //styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  profileForm: FormGroup;

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      name: ['', Validators.required],
      surname: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Učitaj korisnika prilikom inicijalizacije
    this.userService.getAuthenticatedUser().subscribe({
      next: (user: User) => {
        this.user = user;
        this.profileForm.patchValue({
          username: user.username,
          email: user.email,
          name: user.name,
          surname: user.surname
        });
      },
      error: (err) => console.error('Error loading user', err)
    });
  }

  editMode = false;

  // Pokreće editovanje
  enableEditMode(): void {
    this.editMode = true;
  }

  // Zatvara editovanje
  cancelEditMode(): void {
    this.editMode = false;
    if (this.user) {
      this.profileForm.patchValue({
        username: this.user.username,
        email: this.user.email,
        name: this.user.name,
        surname: this.user.surname
      });
    }
  }

  // Ažuriranje korisničkih podataka
  saveChanges(): void {
    if (this.profileForm.valid && this.user) {
      const updatedUser = this.profileForm.value;
      this.userService.updateUser(this.user.id, updatedUser).subscribe({
        next: (updatedUser) => {
          this.user = updatedUser; // Ažuriraj korisnika nakon uspešnog updejta
          this.editMode = false;
        },
        error: (err) => console.error('Error updating user', err)
      });
    }
  }
}