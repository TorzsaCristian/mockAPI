import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IProject } from 'app/entities/project/project.model';

@Component({
  selector: 'jhi-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.scss']
})
export class ProjectDetailsComponent {
  @Input() project: IProject | null = null;

  constructor(protected activatedRoute: ActivatedRoute) { }

  previousState(): void {
    window.history.back();
  }
}
