import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IProject } from 'app/entities/project/project.model';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';

@Component({
  selector: 'jhi-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.scss']
})
export class ProjectDetailsComponent implements OnInit {

  @Input() project: IProject | null = null;

  resources: IResource[] | null = null;

  visible = false;

  dialogResource: IResource | null = null;

  constructor(protected activatedRoute: ActivatedRoute, private resourceService: ResourceService) { }



  ngOnInit(): void {
    if (this.project) {
      this.resourceService.findByProjectId(this.project.id).subscribe((res) => {
        this.resources = res.body;
      }
      );
    }
  }

  viewResource(resource: IResource): void {
    console.warn("View resource: " + resource.id);
    this.dialogResource = resource;
    this.visible = true;
  }

  previousState(): void {
    window.history.back();
  }
}
