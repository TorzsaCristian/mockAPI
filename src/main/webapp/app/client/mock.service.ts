import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "app/core/config/application-config.service";
import { IResource } from "app/entities/resource/resource.model";
import { Observable } from "rxjs";

interface Schema {
  name: string;
  type: string;
  fakerMethod?: string;
}

interface Endpoint {
  enabled: boolean;
  method: string;
  url: string;
  response: string;
}

export interface MockObject {
  name: string;
  generator: string;
  resourceSchema: Schema[];
  endpoints: Endpoint[];
}

@Injectable({
  providedIn: 'root'
})
export class MockService {

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mock-data');


  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) { }


  sendResourceSchemas(projectId: string, serviceObject: MockObject): Observable<MockObject> {
    return this.http.post<MockObject>(`${this.resourceUrl}/project/${projectId}`, serviceObject);
  }

  generateData(resource: IResource): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/generate/${resource.id}`, { observe: 'response' });
  }

}
