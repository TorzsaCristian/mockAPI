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
  dialogResourceMockData: string | null = null;

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
      this.dialogResource = resource;
    }

    this.visible = true;
  }

  openDataViewDialog(resource: IResource | null = null): void {
    this.dataViewVisible = true;
    this.dialogResource = resource;
    this.getMockData(resource!);
    console.warn(resource);
  }

  handleOnHide(): void {
    this.visible = false;
    this.dataViewVisible = false;
    this.dialogResource = null;
    this.dialogResourceMockData = null;
  }

  handleSubmitForm(): void {
    this.handleOnHide();
    this.getResources();
  }

  getMockData(resource: IResource): void {
    this.mockDataService.getMockData(resource.id).subscribe((res: any) => {
      this.dialogResourceMockData = JSON.stringify(res.body, null, 4);
    });
  }

  generateMockData(resource: IResource, count: number): void {
    this.mockDataService.generateMockData(resource.id, count).subscribe((res: any) => {
      console.warn(res);
      // this.dialogResourceMockData = JSON.stringify(res.body, null, 4);
    });
  }

  handleDeleteResource(resource: IResource): void {
    this.resourceService.delete(resource.id).subscribe((res) => {
      this.getResources();
    });
  }

  handleUpdateMockData(resource: IResource, mockData: string): void {
    this.mockDataService.updateMockData(resource.id, mockData).subscribe((res) => {
      // this.getMockData(resource);
      console.warn(res);
      this.handleOnHide();
    });
  }




  previousState(): void {
    window.history.back();
  }
}
