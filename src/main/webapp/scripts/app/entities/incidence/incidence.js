'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('incidence', {
                parent: 'entity',
                url: '/incidences',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.incidence.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/incidence/incidences.html',
                        controller: 'IncidenceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('incidence');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('incidence.detail', {
                parent: 'entity',
                url: '/incidence/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.incidence.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/incidence/incidence-detail.html',
                        controller: 'IncidenceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('incidence');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Incidence', function($stateParams, Incidence) {
                        return Incidence.get({id : $stateParams.id});
                    }]
                }
            })
            .state('incidence.new', {
                parent: 'incidence',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/incidence/incidence-dialog.html',
                        controller: 'IncidenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    creationDate: null,
                                    closed: null,
                                    description: null,
                                    adminComment: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('incidence', null, { reload: true });
                    }, function() {
                        $state.go('incidence');
                    })
                }]
            })
            .state('incidence.edit', {
                parent: 'incidence',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/incidence/incidence-dialog.html',
                        controller: 'IncidenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Incidence', function(Incidence) {
                                return Incidence.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('incidence', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('incidence.delete', {
                parent: 'incidence',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/incidence/incidence-delete-dialog.html',
                        controller: 'IncidenceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Incidence', function(Incidence) {
                                return Incidence.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('incidence', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
