package by.bashlikovvv.core.domain.usecase

import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.core.domain.repository.ILocationsRepository

class GetLocationByIdUseCase(private val locationsRepository: ILocationsRepository) {

    suspend fun execute(id: Int): Location = locationsRepository.getLocationById(id)

}