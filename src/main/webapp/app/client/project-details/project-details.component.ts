import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
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

  constructor(protected activatedRoute: ActivatedRoute, private resourceService: ResourceService, private changeDetector: ChangeDetectorRef) { }



  ngOnInit(): void {
    this.getResources();
  }

  getResources(): void {
    if (this.project) {
      this.resourceService.findByProjectId(this.project.id).subscribe((res) => {
        this.resources = res.body;
      }
      );
    }
  }

  openDialog(resource: IResource | undefined = undefined): void {
    if (resource) {
      console.warn("View resource: " + resource.id);
      this.dialogResource = resource;
      // this.changeDetector.detectChanges();
    }

    this.visible = true;
  }

  handleOnHide(): void {
    this.visible = false;
    this.dialogResource = null;
  }

  handleSubmitForm(): void {
    this.handleOnHide();
    this.getResources();
  }




  previousState(): void {
    window.history.back();
  }
}
