'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('disponibility', {
                parent: 'entity',
                url: '/disponibilitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.disponibility.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/disponibility/disponibilitys.html',
                        controller: 'DisponibilityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('disponibility');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('disponibility.detail', {
                parent: 'entity',
                url: '/disponibility/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.disponibility.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/disponibility/disponibility-detail.html',
                        controller: 'DisponibilityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('disponibility');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Disponibility', function($stateParams, Disponibility) {
                        return Disponibility.get({id : $stateParams.id});
                    }]
                }
            })
            .state('disponibility.new', {
                parent: 'disponibility',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/disponibility/disponibility-dialog.html',
                        controller: 'DisponibilityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    startMoment: null,
                                    endMoment: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('disponibility', null, { reload: true });
                    }, function() {
                        $state.go('disponibility');
                    })
                }]
            })
            .state('disponibility.edit', {
                parent: 'disponibility',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/disponibility/disponibility-dialog.html',
                        controller: 'DisponibilityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Disponibility', function(Disponibility) {
                                return Disponibility.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('disponibility', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('disponibility.delete', {
                parent: 'disponibility',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/disponibility/disponibility-delete-dialog.html',
                        controller: 'DisponibilityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Disponibility', function(Disponibility) {
                                return Disponibility.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('disponibility', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
