package hu.eszfk.emese.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.eszfk.emese.service.dto.AssignmentAdditionalDataDTO;
import hu.eszfk.emese.service.dto.AssignmentDTO;
import hu.eszfk.emese.service.dto.EnvironmentDTO;
import hu.eszfk.emese.service.dto.ItemDTO;
import hu.eszfk.emese.service.dto.MappedApplicationDTO;
import hu.eszfk.emese.service.dto.TriggerDTO;

@Service
@Transactional
public class AssignmentService {

	public List<AssignmentDTO> createAssignmentList(List<EnvironmentDTO> environments) {
		List<AssignmentDTO> assignments = new ArrayList<AssignmentDTO>();
		for (EnvironmentDTO environment : environments) {
			for (MappedApplicationDTO application : environment.getMappedApplications()) {
				if (application.getTriggers() != null) {
					for (TriggerDTO trigger : application.getTriggers()) {
						for (ItemDTO item : trigger.getItems()) {
							if (item.getAssignment() != null) {
								AssignmentDTO assignment = null;
								try {
									/**
									 * clone because i dont want to write the cache
									 */
									assignment = (AssignmentDTO) item.getAssignment().clone();
								} catch (CloneNotSupportedException e) {
									continue;
								}
								AssignmentAdditionalDataDTO additionalData = new AssignmentAdditionalDataDTO();
								additionalData.setEnvironmentId(environment.getId());
								additionalData.setEnvironmentName(environment.getName());
								additionalData.setHostId(trigger.getHost().getId());
								additionalData.setHostName(trigger.getHost().getName());
								additionalData.setItemId(item.getId());
								additionalData.setItemName(item.getName());
								additionalData.setMappedApplicationName(application.getName());
								additionalData.setTriggerId(trigger.getId());
								additionalData.setTriggerName(trigger.getName());
								additionalData.setPriority(trigger.getPriority());
								assignment.setAdditionalData(additionalData);
								assignments.add(assignment);
							}
						}
					}
				}
			}
		}
		return assignments;
	}
}
