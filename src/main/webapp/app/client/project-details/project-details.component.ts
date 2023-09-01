import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IProject } from 'app/entities/project/project.model';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';
import { MockService } from '../mock.service';

@Component({
  selector: 'jhi-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.scss']
})
export class ProjectDetailsComponent implements OnInit {

  @Input() project: IProject | null = null;

  resources: IResource[] | null = null;

  visible = false;
  dataViewVisible = false;

  dialogResource: IResource | null = null;

  constructor(protected activatedRoute: ActivatedRoute, private resourceService: ResourceService, private mockDataService: MockService, private changeDetector: ChangeDetectorRef) { }



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

  openDataViewDialog(resource: IResource | undefined = undefined): void {
    this.dataViewVisible = true;
    this.generateData(resource!);
    console.warn(resource);
  }

  handleOnHide(): void {
    this.visible = false;
    this.dataViewVisible = false;
    this.dialogResource = null;
  }

  handleSubmitForm(): void {
    this.handleOnHide();
    this.getResources();
  }

  generateData(resource: IResource): void {
    this.mockDataService.generateData(resource).subscribe((res: any) => {
      console.warn(res);
      // this.getResources();
    });
  }




  previousState(): void {
    window.history.back();
  }
}
