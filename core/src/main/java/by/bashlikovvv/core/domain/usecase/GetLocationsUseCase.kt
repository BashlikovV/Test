package by.bashlikovvv.core.domain.usecase

import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.core.domain.repository.ILocationsRepository

class GetLocationsUseCase(private val locationsRepository: ILocationsRepository) {

    suspend fun execute(): List<Location> = locationsRepository.getLocations()

}