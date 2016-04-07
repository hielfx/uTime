'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('availability', {
                parent: 'entity',
                url: '/availabilitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.availability.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/availability/availabilitys.html',
                        controller: 'AvailabilityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('availability');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('availability.detail', {
                parent: 'entity',
                url: '/availability/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.availability.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/availability/availability-detail.html',
                        controller: 'AvailabilityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('availability');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Availability', function ($stateParams, Availability) {
                        return Availability.get({id: $stateParams.id});
                    }]
                }
            })
            .state('availability.new', {
                parent: 'availability',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/availability/availability-dialog.html',
                        controller: 'AvailabilityDialogController',
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
                            $state.go('availability', null, {reload: true});
                    }, function() {
                            $state.go('availability');
                    })
                }]
            })
            .state('availability.edit', {
                parent: 'availability',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/availability/availability-dialog.html',
                        controller: 'AvailabilityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Availability', function (Availability) {
                                return Availability.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('availability', null, {reload: true});
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('availability.delete', {
                parent: 'availability',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/availability/availability-delete-dialog.html',
                        controller: 'AvailabilityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Availability', function (Availability) {
                                return Availability.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('availability', null, {reload: true});
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
