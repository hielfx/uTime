'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('curriculum', {
                parent: 'entity',
                url: '/curriculums',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.curriculum.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/curriculum/curriculums.html',
                        controller: 'CurriculumController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('curriculum');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('curriculum.detail', {
                parent: 'entity',
                url: '/curriculum/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.curriculum.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/curriculum/curriculum-detail.html',
                        controller: 'CurriculumDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('curriculum');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Curriculum', function($stateParams, Curriculum) {
                        return Curriculum.get({id : $stateParams.id});
                    }]
                }
            })
            .state('curriculum.new', {
                parent: 'curriculum',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/curriculum/curriculum-dialog.html',
                        controller: 'CurriculumDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    website: null,
                                    creationDate: null,
                                    modificationDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('curriculum', null, { reload: true });
                    }, function() {
                        $state.go('curriculum');
                    })
                }]
            })
            .state('curriculum.edit', {
                parent: 'curriculum',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/curriculum/curriculum-dialog.html',
                        controller: 'CurriculumDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Curriculum', function(Curriculum) {
                                return Curriculum.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('curriculum', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('curriculum.delete', {
                parent: 'curriculum',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/curriculum/curriculum-delete-dialog.html',
                        controller: 'CurriculumDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Curriculum', function(Curriculum) {
                                return Curriculum.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('curriculum', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
