package by.bashlikovvv.core.domain.usecase

import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.core.domain.repository.ILocationsRepository

class CheckLocationsDataChangedUseCase(private val locationsRepository: ILocationsRepository) {

    suspend fun execute(preValue: List<Location>): Boolean =
        locationsRepository.checkDataChanged(preValue)

}