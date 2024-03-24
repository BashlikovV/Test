package by.bashlikovvv.core.domain.usecase

import by.bashlikovvv.core.domain.repository.ILocationsRepository

class GetSectionNameUseCase(private val locationsRepository: ILocationsRepository) {

    suspend fun execute() = locationsRepository.getSection()

}