import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import MainComponent from './main.component';
import FooterComponent from '../footer/footer.component';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { ProjectListComponent } from 'app/client/project-list/project-list.component';
import { FormatMediumDatePipe } from 'app/shared/date';
import { EndpointFormComponent } from 'app/client/endpoint-form/endpoint-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ProjectDetailsComponent } from 'app/client/project-details/project-details.component';
import { ResourceListItemComponent } from 'app/client/resource-list-item/resource-list-item.component';

@NgModule({
  imports: [SharedModule, RouterModule, FormsModule, InputTextareaModule, ReactiveFormsModule, FooterComponent, PageRibbonComponent, FormatMediumDatePipe],
  declarations: [MainComponent, ProjectListComponent, EndpointFormComponent, ProjectDetailsComponent, ResourceListItemComponent],
})
export default class MainModule { }
