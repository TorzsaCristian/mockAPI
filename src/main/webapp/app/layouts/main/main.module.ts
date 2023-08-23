import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import MainComponent from './main.component';
import FooterComponent from '../footer/footer.component';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { ProjectListComponent } from 'app/project-list/project-list.component';
import { FormatMediumDatePipe } from 'app/shared/date';

@NgModule({
  imports: [SharedModule, RouterModule, FooterComponent, PageRibbonComponent, FormatMediumDatePipe, FormatMediumDatePipe],
  declarations: [MainComponent, ProjectListComponent],
})
export default class MainModule { }
